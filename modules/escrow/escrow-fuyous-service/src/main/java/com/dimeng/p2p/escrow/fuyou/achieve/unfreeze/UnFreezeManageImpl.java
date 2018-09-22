package com.dimeng.p2p.escrow.fuyou.achieve.unfreeze;

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
import com.dimeng.p2p.escrow.fuyou.cond.UnFreezeCond;
import com.dimeng.p2p.escrow.fuyou.entity.enums.T6170_F06;
import com.dimeng.p2p.escrow.fuyou.entity.freeze.T6170;
import com.dimeng.p2p.escrow.fuyou.entity.unfreeze.UnFreezeRet;
import com.dimeng.p2p.escrow.fuyou.service.unfreeze.UnFreezeManage;
import com.dimeng.util.StringHelper;
import com.google.gson.Gson;

/**
 * 资金解冻管理
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
public class UnFreezeManageImpl extends AbstractEscrowService implements UnFreezeManage
{
    
    public UnFreezeManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public boolean updateT6101(UnFreezeRet unFreeze)
        throws Throwable
    {
        boolean flag = false;
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                //成功解冻金额
                BigDecimal amt = formatAmtRet(unFreeze.getSucAmt());
                //用户登录帐号
                String userAccount = unFreeze.getCustNo();
                //往来账户余额
                BigDecimal wlzhAmt = selectT6101(userAccount, T6101_F03.WLZH.toString()).add(amt);
                //锁定账户余额
                BigDecimal sdzhAmt = selectT6101(userAccount, T6101_F03.SDZH.toString()).subtract(amt);
                //更新往来账户金额
                flag = updateT6101ToWlzh(connection, userAccount, wlzhAmt);
                //更新锁定账户金额
                flag = updateT6101ToSdzh(connection, userAccount, sdzhAmt);
                //如果当前时间小于设定的解冻时间，则是手动解冻，不然则是定时任务自动解冻
                Timestamp dateNow = getCurrentTimestamp(connection);
                if (dateNow.compareTo(unFreeze.getThawDate()) == -1)
                {
                    unFreeze.setThawDate(dateNow);
                }
                //更新冻结记录为已解冻
                flag = updateT6170(connection, unFreeze);
                flag = true;
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                logger.error("资金解冻异常，回滚平台数据！" + e);
                serviceResource.rollback(connection);
                throw e;
            }
        }
        
        return flag;
    }
    
    /**
     * 更新冻结记录为已解冻，保存解冻流水号
     * @param unFreeze
     * @return
     * @throws Throwable
     */
    public boolean updateT6170(Connection connection, UnFreezeRet unFreeze)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6170 SET T6170.F05 = ? ,T6170.F06 = 'YJD' , F07 = ? WHERE T6170.F02 = ?"))
        {
            pstmt.setTimestamp(1, unFreeze.getThawDate());
            pstmt.setString(2, unFreeze.getMchntTxnSsn());
            pstmt.setString(3, unFreeze.getFreezeSerialNo());
            pstmt.execute();
        }
        return true;
    }
    
    @Override
    public T6101 selectT6101(String name)
        throws Throwable
    {
        T6101 record = new T6101();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F05, F06 FROM S61.T6101 WHERE T6101.F05 = ? AND T6101.F03='WLZH'"))
            {
                pstmt.setString(1, name);
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
    
    /**
     * 更新锁定账户金额
     * @param connection
     * @param sdye
     * @return
     * @throws Throwable
     */
    public boolean updateT6101ToSdzh(Connection connection, String platformUserNo, BigDecimal sdye)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = CURRENT_TIMESTAMP() WHERE F05 = ? and F03='"
                + T6101_F03.SDZH + "'"))
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
            connection.prepareStatement("UPDATE S61.T6101 SET F06 = ?, F07 = CURRENT_TIMESTAMP() WHERE F05 = ? AND F03 = '"
                + T6101_F03.WLZH + "'"))
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
                connection.prepareStatement("SELECT F06 FROM S61.T6101 WHERE T6101.F05 = ? AND T6101.F03=?"))
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
    public UnFreezeRet unfreezeRetDecoder(HashMap<String, Object> xmlMap, String plain)
        throws Throwable
    {
        logger.info("资金解冻-UnFreezeManageImpl.unfreezeRetDecoder() start...");
        logger.info("资金解冻返回参数值：plain = " + plain);
        logger.info("资金解冻返回参数值：signature = " + xmlMap.get("signature"));
        
        boolean verifyResult = verifyByRSA(plain, (String)(xmlMap.get("signature")));
        logger.info("验签结果：" + verifyResult);
        
        if (verifyResult)
        {
            UnFreezeRet entity = new UnFreezeRet();
            // 响应码
            entity.setRespCode(xmlMap.get("resp_code").toString());
            // 商户代码
            entity.setMchntCd(xmlMap.get("mchnt_cd").toString());
            // 请求流水号
            entity.setMchntTxnSsn(xmlMap.get("mchnt_txn_ssn").toString());
            // 请求解冻金额
            entity.setAmt(xmlMap.get("amt").toString());
            // 成功解冻金额
            entity.setSucAmt(xmlMap.get("suc_amt").toString());
            // 签名数据
            entity.setSignature(xmlMap.get("signature").toString());
            
            logger.info("资金解冻-UnFreezeManageImpl.unfreezeRetDecoder() end..." + entity);
            return entity;
        }
        else
        {
            logger.info("资金解冻-UnFreezeManageImpl.unfreezeRetDecoder() start...NULL --- 验签失败！");
            return null;
        }
    }
    
    /**
     * 第三方账号查询
     * <功能详细描述>
     * @param userCustId
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
    
    @Override
    public PagingResult<T6170> getT6170(String name, String serialNumber, String status, Paging paging)
        throws Throwable
    {
        StringBuilder sb = new StringBuilder("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6170 WHERE T6170.F03 = ?");
        List<Object> params = new ArrayList<>();
        params.add(name);
        if (!StringHelper.isEmpty(serialNumber))
        {
            sb.append(" AND T6170.F02 LIKE ?");
            params.add("%" + serialNumber + "%");
        }
        if (!StringHelper.isEmpty(status))
        {
            sb.append(" AND T6170.F06 = ?");
            params.add(status);
        }
        sb.append("ORDER BY F06 ASC");
        try (Connection connection = getConnection())
        {
            return selectPaging(connection, new ArrayParser<T6170>()
            {
                @Override
                public T6170[] parse(ResultSet resultSet)
                    throws SQLException
                {
                    ArrayList<T6170> list = null;
                    while (resultSet.next())
                    {
                        T6170 record = new T6170();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = T6170_F06.parse(resultSet.getString(6));
                        if (list == null)
                        {
                            list = new ArrayList<>();
                        }
                        list.add(record);
                    }
                    return ((list == null || list.size() == 0) ? null : list.toArray(new T6170[list.size()]));
                }
            }, paging, sb.toString(), params);
        }
    }
    
    /** {@inheritDoc} */
    
    @Override
    public T6170 selectT6170(String serialNo)
        throws Throwable
    {
        T6170 record = null;
        try (Connection connection = this.getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06  FROM S61.T6170 WHERE T6170.F02 = ?"))
            {
                pstmt.setString(1, serialNo);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6170();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getBigDecimal(4);
                        record.F05 = resultSet.getTimestamp(5);
                        record.F06 = T6170_F06.parse(resultSet.getString(6));
                    }
                }
            }
        }
        return record;
    }
    
    /** {@inheritDoc} */
    
    @Override
    public Map<String, String> createUnFreeze(UnFreezeCond cond)
        throws Throwable
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mchnt_cd", cond.mchntCd());
        param.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        param.put("cust_no", cond.custNo());
        param.put("amt", cond.amt());
        param.put("rem", cond.rem());
        logger.info("拼接字符：" + new Gson().toJson(param));
        String str = getSignature(param);
        logger.info("拼接=" + str);
        String signature = encryptByRSA(str);
        logger.info("signature == " + signature);
        param.put("signature", signature);
        return param;
    }
    
    @Override
    public T6170[] getT6170s()
        throws Throwable
    {
        List<T6170> list = new ArrayList<T6170>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement sts =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06 FROM S61.T6170 WHERE F06 = 'DJZ' AND F05 <= CURRENT_TIMESTAMP()"))
            {
                try (ResultSet rs = sts.executeQuery())
                {
                    while (rs.next())
                    {
                        T6170 record = new T6170();
                        record.F01 = rs.getInt(1);
                        record.F02 = rs.getString(2);
                        record.F03 = rs.getString(3);
                        record.F04 = rs.getBigDecimal(4);
                        record.F05 = rs.getTimestamp(5);
                        record.F06 = T6170_F06.parse(rs.getString(6));
                        list.add(record);
                    }
                }
            }
        }
        T6170[] t6170s = new T6170[list.size()];
        return list.toArray(t6170s);
    }
    
}
