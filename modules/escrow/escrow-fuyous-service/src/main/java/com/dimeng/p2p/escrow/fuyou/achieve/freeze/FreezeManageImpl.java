package com.dimeng.p2p.escrow.fuyou.achieve.freeze;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.query.ArrayParser;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.escrow.fuyou.achieve.AbstractEscrowService;
import com.dimeng.p2p.escrow.fuyou.cond.FreezeCond;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.FYT6101;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.Freeze;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.FreezeRet;
import com.dimeng.p2p.escrow.fuyou.service.freeze.FreezeManage;
import com.dimeng.util.StringHelper;
import com.google.gson.Gson;

/**
 * 资金冻结管理
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
public class FreezeManageImpl extends AbstractEscrowService implements FreezeManage
{
    
    public FreezeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public PagingResult<FYT6101> getT6101(String name, Paging paging)
        throws Throwable
    {
        StringBuilder sb =
            new StringBuilder(
                "SELECT T6101.F01 AS F01, T6101.F02 AS F02, T6101.F05 AS F03, T6101.F06 AS F04, T6118.F06 AS F05, T6141.F02 AS F06 FROM S61.T6101 "
                    + "LEFT JOIN S61.T6119 ON T6119.F01 = T6101.F02 LEFT JOIN S61.T6118 ON T6118.F01 = T6101.F02 LEFT JOIN S61.T6141 ON T6141.F01 = T6101.F02 "
                    + "WHERE T6101.F03 = 'WLZH' AND T6119.F03 != '' AND T6101.F01 != (SELECT F01 FROM S71.T7101 LIMIT 1)");
        List<Object> params = new ArrayList<>();
        if (!StringHelper.isEmpty(name))
        {
            sb.append(" AND T6101.F05 LIKE ?");
            params.add("%" + name + "%");
        }
        sb.append("ORDER BY T6101.F07 DESC");
        
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<FYT6101>()
            {
                @Override
                public FYT6101[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<FYT6101> list = null;
                    while (resultSet.next())
                    {
                        FYT6101 record = new FYT6101();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F05 = resultSet.getString(3);
                        record.F06 = resultSet.getBigDecimal(4);
                        record.F08 = resultSet.getString(5);
                        record.F09 = resultSet.getString(6);
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new FYT6101[list.size()]));
                }
            }, paging, sb.toString(), params);
        }
    }
    
    @Override
    public boolean updateT6101(Freeze freeze)
        throws Throwable
    {
        boolean flag = false;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                String userAccount = freeze.getCustNo();
                
                //往来账户余额
                BigDecimal wlzh =
                    selectT6101(userAccount, T6101_F03.WLZH.toString()).subtract(new BigDecimal(freeze.getAmt()));
                
                //锁定账户余额
                BigDecimal sdzh =
                    new BigDecimal(freeze.getAmt()).add(selectT6101(freeze.getCustNo(), T6101_F03.SDZH.toString()));
                
                //更新用户往来账户
                flag = updateT6101ToWlzh(connection, userAccount, wlzh);
                
                //更新用户锁定账户
                flag = updateT6101ToSdzh(connection, userAccount, sdzh);
                
                //新增用户冻结记录
                flag = insertT6170(connection, freeze);
                
                flag = true;
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                logger.error("资金冻结异常，回滚平台数据！" + e);
                serviceResource.rollback(connection);
                throw e;
            }
        }
        
        return flag;
    }
    
    /**
     * 往T6170添加数据
     * @param freeze
     * @return
     * @throws Throwable
     */
    public boolean insertT6170(Connection connection, Freeze freeze)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("insert into S61.T6170 set F02=?,F03=?,F04=?,F05=?"))
        {
            pstmt.setString(1, freeze.getMchntTxnSsn());
            pstmt.setString(2, freeze.getCustNo());
            pstmt.setString(3, freeze.getAmt());
            pstmt.setTimestamp(4, Timestamp.valueOf(freeze.getExpired()));
            pstmt.execute();
        }
        return true;
    }
    
    @Override
    public T6101 selectT6101(String name, T6101_F03 F03)
        throws Throwable
    {
        T6101 record = new T6101();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F05, F06 FROM S61.T6101 WHERE T6101.F05 = ? AND T6101.F03 = ?"))
            {
                pstmt.setString(1, name);
                pstmt.setString(2, F03.toString());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    while (resultSet.next())
                    {
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getInt(2);
                        record.F05 = resultSet.getString(3);
                        record.F06 = resultSet.getBigDecimal(4);
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public Map<String, String> createFreeze(FreezeCond cond)
        throws Throwable
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mchnt_cd", cond.mchntCd());
        param.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        param.put("cust_no", cond.custNo());
        param.put("amt", cond.amt());
        param.put("rem", cond.rem());
        logger.info("资金冻结拼接字符：" + new Gson().toJson(param));
        String str = getSignature(param);
        logger.info("资金冻结拼接=" + str);
        String signature = encryptByRSA(str);
        logger.info("资金冻结signature == " + signature);
        param.put("signature", signature);
        return param;
    }
    
    /**
     * 更新锁定账户金额
     * @param connection
     * @param freeze
     * @param sdye
     * @return
     * @throws Throwable
     */
    public boolean updateT6101ToSdzh(Connection connection, String platformUserNo, BigDecimal sdye)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = CURRENT_TIMESTAMP() WHERE F05 = ? and F03='SDZH'"))
        {
            pstmt.setBigDecimal(1, sdye);
            pstmt.setString(2, platformUserNo);
            pstmt.execute();
        }
        
        return true;
    }
    
    /**
     * 更新往来账户金额
     * @param connection
     * @param freeze
     * @param money
     * @return
     * @throws Throwable
     */
    public boolean updateT6101ToWlzh(Connection connection, String platformUserNo, BigDecimal money)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = CURRENT_TIMESTAMP() WHERE F05 = ? and F03='WLZH'"))
        {
            pstmt.setBigDecimal(1, money);
            pstmt.setString(2, platformUserNo);
            pstmt.execute();
        }
        return true;
    }
    
    /**
     * 查询金额
     * @param name
     * @param type
     * @return
     * @throws Throwable
     */
    public BigDecimal selectT6101(String name, String type)
        throws Throwable
    {
        BigDecimal bigDecimal = null;
        try (Connection connection = this.getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F06 FROM S61.T6101 WHERE T6101.F05 = ? and T6101.F03=?"))
            {
                pstmt.setString(1, name);
                pstmt.setString(2, type);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        bigDecimal = resultSet.getBigDecimal(1);
                    }
                }
            }
        }
        return bigDecimal;
    }
    
    @Override
    public FreezeRet freezeReturnDecoder(HashMap<String, Object> xmlMap, String plain)
        throws Throwable
    {
        logger.info("FreezeManageImpl.freezeReturnDecoder() start...");
        logger.info("返回参数值：plain = " + plain);
        logger.info("返回参数值：signature = " + xmlMap.get("signature"));
        
        boolean verifyResult = verifyByRSA(plain, (String)(xmlMap.get("signature")));
        logger.info("验签结果：" + verifyResult);
        
        if (verifyResult)
        {
            FreezeRet entity = new FreezeRet();
            // 响应码
            entity.setRespCode(xmlMap.get("resp_code").toString());
            // 商户代码
            entity.setMchntCd(xmlMap.get("mchnt_cd").toString());
            // 请求流水号
            entity.setMchntTxnSsn(xmlMap.get("mchnt_txn_ssn").toString());
            // 签名数据
            entity.setSignature(xmlMap.get("signature").toString());
            
            logger.info("FreezeManageImpl.freezeReturnDecoder() end..." + entity);
            return entity;
        }
        else
        {
            logger.info("FreezeManageImpl.freezeReturnDecoder() start...NULL --- 验签失败！");
            return null;
        }
    }
    
    /**
     * 第三方账号查询
     * <功能详细描述>
     * @param userAccount
     * @return
     * @throws Throwable
     */
    @Override
    public String getAccountId(String userAccount)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6119.F03 FROM S61.T6119 LEFT JOIN S61.T6110 ON T6110.F01 = T6119.F01 WHERE T6110.F02 = ?"))
            {
                pstmt.setString(1, userAccount);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return resultSet.getString(1);
                    }
                }
            }
            return null;
        }
    }
    
    @Override
    public boolean verifyByRSA(String plain, String chkValue)
        throws Exception
    {
        return super.verifyByRSA(plain, chkValue);
    }
    
}
