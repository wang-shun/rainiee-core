package com.dimeng.p2p.escrow.fuyou.achieve;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S61.entities.T6114_EXT;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.escrow.fuyou.cond.BankCond;
import com.dimeng.p2p.escrow.fuyou.cond.BankQueryCond;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouRespCode;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.face.BankQueryFace;
import com.dimeng.p2p.escrow.fuyou.face.UserinfoQuery;
import com.dimeng.p2p.escrow.fuyou.service.BankManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.google.gson.Gson;

/**
 * 
 * 更新用户银行卡
 * <第三方注册成功，但平台更新失败 的异常处理 >
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月15日]
 */
public class BankManageImpl extends AbstractEscrowService implements BankManage
{
    
    public BankManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public HashMap<String, String> getUsrCustInfo()
        throws Throwable
    {
        HashMap<String, String> params = new HashMap<String, String>();
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03 FROM S61.T6119 WHERE T6119.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        params.put("usrCustId", resultSet.getString(1));
                    }
                }
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F07 FROM S61.T6114 WHERE T6114.F02 = ? AND T6114.F08 = 'QY' LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        params.put("bankId", String.valueOf(resultSet.getInt(1)));
                        params.put("bank", resultSet.getString(2));
                    }
                }
            }
            if (!StringHelper.isEmpty(params.get("usrCustId")))
            {
                try (PreparedStatement pstmt =
                    connection.prepareStatement("SELECT F06 FROM S61.T6110 WHERE T6110.F01 = ? AND T6110.F07 = 'QY' LIMIT 1"))
                {
                    pstmt.setInt(1, serviceResource.getSession().getAccountId());
                    try (ResultSet resultSet = pstmt.executeQuery())
                    {
                        if (resultSet.next())
                        {
                            params.put("ZRR", resultSet.getString(1));
                        }
                    }
                }
            }
        }
        return params;
    }
    
    @Override
    public UserQueryEntity userChargeQuery(String mchnt_cd, String user_ids)
        throws Throwable
    {
        UserQueryEntity userQueryEntity = new UserQueryEntity();
        // 商户代码
        userQueryEntity.setMchnt_cd(mchnt_cd);
        // 交易日期
        userQueryEntity.setMchnt_txn_dt(new SimpleDateFormat("yyyyMMdd").format((new Timestamp(
            System.currentTimeMillis()))));
        // 流水号
        userQueryEntity.setMchnt_txn_ssn(MchntTxnSsn.getMts(FuyouTypeEnum.YHXX.name()));
        // 待查询的登录帐户列表
        userQueryEntity.setUser_ids(user_ids);
        List<String> params = new ArrayList<>();
        params.add(userQueryEntity.getMchnt_cd());
        params.add(userQueryEntity.getMchnt_txn_dt());
        params.add(userQueryEntity.getUser_ids());
        logger.info(userQueryEntity.getMchnt_cd());
        logger.info(userQueryEntity.getMchnt_txn_dt());
        logger.info(userQueryEntity.getUser_ids());
        // 获取签名字符串
        String ChkValue = chkValue(params);
        logger.info("signature == " + ChkValue);
        // 签名数据
        userQueryEntity.setSignature(ChkValue);
        return userQueryEntity;
    }
    
    @Override
    public boolean updateBank(HashMap<String, String> params, boolean flag)
        throws Throwable
    {
        String capAcntNo = params.get("capAcntNo");
        int orederId =
            insertT6114(serviceResource.getSession().getAccountId(),
                params.get("parent_bank_id"),
                IntegerParser.parse(params.get("city_id")),
                params.get("bank_nm"),
                params.get("capAcntNo").replace(capAcntNo.substring(4, capAcntNo.length() - 4),
                    getTargetStr(capAcntNo.length() - 7)),
                StringHelper.encode(capAcntNo),
                params.get("cust_nm"),
                flag);
        if (orederId > 0)
        {
            return true;
        }
        return false;
    }
    
    /**
     * 插入开户行地区代码、开户行支行名称、帐号、加密帐号
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @param F04
     * @param F05
     * @throws SQLException
     */
    private int insertT6114(int F02, String F03, int F04, String F05, String F06, String F07, String name, boolean flag)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T6114.F01 FROM S61.T6114 WHERE T6114.F02= ? AND T6114.F08 = 'QY'"))
            {
                pstmt.setInt(1, F02);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        return 0;
                    }
                }
            }
            StringBuffer sql = new StringBuffer();
            if (flag)
            {
                sql.append("INSERT INTO S61.T6114 SET F02 = ?, F03 = (SELECT F01 FROM S50.T5020 WHERE F04 = ? LIMIT 1),");
                sql.append("F04 = (SELECT F01 FROM S50.T5019 WHERE F03 = ? LIMIT 1), F05 = ?, F06 = ?,");
                sql.append("F07 = ?, F08 = 'QY', F09 = CURRENT_TIMESTAMP(), F10 = 'TG', F11=?, F12= 1 ");
            }
            else
            {
                sql.append("INSERT INTO S61.T6114 SET F02 = ?, ");
                sql.append("F03 = (SELECT F01 FROM S50.T5020 WHERE F04 = ? LIMIT 1), " + "F04 = ? , F05 = ?, F06 = ?, ");
                sql.append("F07 = ?, F08 = 'QY', F09 = CURRENT_TIMESTAMP(), F10 = 'TG',F11= ?,F12= 2 ");
            }
            try (PreparedStatement pstmt =
                connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS))
            {
                pstmt.setInt(1, F02);
                pstmt.setString(2, F03);
                pstmt.setInt(3, F04);
                pstmt.setString(4, F05);
                pstmt.setString(5, F06);
                pstmt.setString(6, F07);
                pstmt.setString(7, name);
                pstmt.execute();
                try (ResultSet resultSet = pstmt.getGeneratedKeys();)
                {
                    if (resultSet.next())
                    {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            }
        }
    }
    
    /**
     * 获取一个*序列
     * 
     * @param length
     * @return
     */
    private String getTargetStr(int length)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            sb.append("*");
        }
        return sb.toString();
    }
    
    @Override
    public Map<String, String> bankUpdate(BankCond cond)
        throws Throwable
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mchnt_cd", cond.mchntCd());
        param.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        param.put("login_id", cond.loginId());
        param.put("page_notify_url", cond.pageNotifyUrl());
        logger.info("修改银行卡：" + new Gson().toJson(param));
        String str = getSignature(param);
        String signature = encryptByRSA(str);
        logger.info("signature == " + signature);
        param.put("signature", signature);
        return param;
    }
    
    @Override
    public boolean verifyByRSA(String plain, String chkValue)
        throws Exception
    {
        return super.verifyByRSA(plain, chkValue);
    }
    
    @Override
    public String selectT5020(int F01)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT T5020.F04 FROM S50.T5020 WHERE T5020.F01= ? AND T5020.F03 = 'QY'"))
            {
                pstmt.setInt(1, F01);
                try (ResultSet rs = pstmt.executeQuery())
                {
                    if (rs.next())
                    {
                        return rs.getString(1);
                    }
                }
            }
        }
        return null;
    }
    
    @Override
    public Map<String, String> bankQuery(BankQueryCond cond)
        throws Throwable
    {
        Map<String, String> param = new HashMap<String, String>();
        param.put("mchnt_cd", cond.mchntCd());
        param.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        param.put("login_id", cond.loginId());
        param.put("txn_ssn", cond.txnSsn());
        String signature = encryptByRSA(getSignature(param));
        param.put("signature", signature);
        return param;
    }
    
    @Override
    public void insertT6114Ext(int bankId, String mchnt_txn_ssn)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("INSERT INTO S61.T6114_EXT SET F02 = ?, F03 = ?, F05 = CURRENT_TIMESTAMP(), F08 = ? "))
            {
                pstmt.setInt(1, bankId);
                pstmt.setInt(2, serviceResource.getSession().getAccountId());
                pstmt.setString(3, mchnt_txn_ssn);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public boolean bankRetDecode(Map<String, String> params)
        throws Throwable
    {
        Map<String, String> paramMap = null;
        
        if (params != null)
        {
            paramMap = new HashMap<String, String>();
            //商户代码
            paramMap.put("mchnt_cd", params.get("mchnt_cd"));
            //请求流水号
            paramMap.put("mchnt_txn_ssn", params.get("mchnt_txn_ssn"));
            //响应码
            paramMap.put("resp_code", params.get("resp_code"));
            //响应消息
            paramMap.put("resp_desc", params.get("resp_desc"));
            //签名数据
            paramMap.put("signature", params.get("signature"));
        }
        
        String str = getSignature(paramMap);
        if (!verifyByRSA(str, params.get("signature")))
        {
            logger.info("更换银行卡申请返回：" + BackCodeInfo.info(params.get("resp_code")));
            return false;
        }
        return true;
    }
    
    @Override
    public void updateT6114Ext(Map<String, String> params)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                try (PreparedStatement psmt =
                    connection.prepareStatement("UPDATE S61.T6114_EXT SET F04 = ?, F06 = CURRENT_TIMESTAMP(), F09 = ? WHERE F08 = ? "))
                {
                    psmt.setString(1,
                        FuyouRespCode.JYCG.getRespCode().equals(params.get("resp_code")) ? T6501_F03.YTJ.name()
                            : T6501_F03.SB.name());
                    psmt.setString(2, FuyouRespCode.JYCG.getRespCode().equals(params.get("resp_code")) ? "已提交申请"
                        : "申请失败");
                    psmt.setString(3, params.get("mchnt_txn_ssn"));
                    psmt.execute();
                }
                updateT6114(connection, T6114_F08.TY.name(), params.get("mchnt_txn_ssn"));
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
    }
    
    @Override
    public boolean selectT6114Ext()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ppsmt =
                connection.prepareStatement("SELECT F04 FROM S61.T6114_EXT WHERE F03 = ? ORDER BY F01 DESC  LIMIT 1"))
            {
                ppsmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = ppsmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        String rst = resultSet.getString(1);
                        if (T6501_F03.CG.name().equals(rst) || T6501_F03.SB.name().equals(rst))
                        {
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                        
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public T6114_EXT selectT6114Ext(String mchnt_txn_ssn)
        throws Throwable
    {
        T6114_EXT t6114_EXT = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement ppsmt =
                connection.prepareStatement("SELECT F01, F03, F04, F08 FROM S61.T6114_EXT WHERE F03 = ? ORDER BY F01 DESC  LIMIT 1"))
            {
                ppsmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = ppsmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6114_EXT = new T6114_EXT();
                        t6114_EXT.F01 = resultSet.getInt(1);
                        t6114_EXT.F03 = resultSet.getInt(2);
                        t6114_EXT.F04 = T6501_F03.parse(resultSet.getString(3));
                        t6114_EXT.F08 = resultSet.getString(4);
                    }
                }
            }
        }
        return t6114_EXT;
    }
    
    @Override
    public String queryFuyou(ServiceSession serviceSession, String mchnt_txn_ssn, String mchnt_cd, String url,
        boolean flag)
        throws Throwable
    {
        BankQueryFace face = new BankQueryFace();
        Map<String, Object> parma =
            face.executeBankQuery(mchnt_cd,
                MchntTxnSsn.getMts(FuyouTypeEnum.YHXX.name()),
                getAccountId(serviceSession.getSession().getAccountId()),
                mchnt_txn_ssn,
                url,
                serviceSession);
        if (parma == null)
        {
            logger.info("验签失败");
            return "验签失败";
        }
        String rem = null;
        String resp_code = parma.get("resp_code").toString();
        if (!FuyouRespCode.JYCG.getRespCode().equals(resp_code))
        {
            if (flag)
            {
                rem = BackCodeInfo.info(resp_code);
            }
            else
            {
                logger.info(parma.get("desc_code"));
                rem = "未提交申请";
                try (Connection connection = getConnection())
                {
                    updateT6114Ext(connection, T6501_F03.SB.name(), "未提交申请", mchnt_txn_ssn);
                }
            }
            logger.info(rem);
            return rem;
        }
        switch (Integer.parseInt(parma.get("examine_st").toString()))
        {
        // 待审核
            case 0:
                rem = "待审核...";
                break;
            // 审核成功
            case 1:
                if (updateT6114Ext(serviceSession, true, mchnt_txn_ssn))
                {
                    rem = "OK";
                }
                else
                {
                    rem = "系统更新失败";
                }
                break;
            // 审核失败
            case 2:
                rem = "SHSB";
                updateT6114Ext(serviceSession, false, mchnt_txn_ssn);
                break;
        }
        
        return rem;
    }
    
    private boolean updateT6114Ext(ServiceSession serviceSession, boolean flag, String mchnt_txn_ssn)
        
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                if (flag)
                {
                    // 审核成功，则通知查询用户信息更新用户银行卡
                    // 至 富友查询用户信息
                    UserinfoQuery userinfoQuery = new UserinfoQuery();
                    final UserQueryResponseEntity userQuery =
                        userinfoQuery.userinfoQuery(userChargeQuery(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID),
                            selectT6119(connection, serviceResource.getSession().getAccountId())),
                            serviceSession,
                            configureProvider.format(FuyouVariable.FUYOU_QUERYUSERINFS_URL));
                    if (!FuyouRespCode.JYCG.getRespCode().equals(userQuery.getResp_code()))
                    {
                        logger.info("用户信息：查询信息失败");
                        throw new LogicalException("查询信息失败！");
                    }
                    HashMap<String, String> params = new HashMap<String, String>();
                    // 姓名
                    params.put("cust_nm", userQuery.getCust_nm());
                    // 身份证
                    params.put("certif_id", userQuery.getCertif_id());
                    // 邮箱
                    params.put("email", userQuery.getEmail());
                    // 开户行地区代码
                    params.put("city_id", userQuery.getCity_id());
                    // 开户行行别
                    params.put("parent_bank_id", userQuery.getParent_bank_id());
                    // 开户支行名称
                    params.put("bank_nm", userQuery.getBank_nm());
                    // 银行卡账号
                    params.put("capAcntNo", userQuery.getCapAcntNo());
                    if (updateBank(params, true))
                    {
                        updateT6114Ext(connection, T6501_F03.CG.name(), "成功更换银行卡", mchnt_txn_ssn);
                        serviceResource.commit(connection);
                        return true;
                    }
                }
                else
                {
                    // 审核未通过，则启用原银行卡
                    updateT6114(connection, T6114_F08.QY.name(), mchnt_txn_ssn);
                    updateT6114Ext(connection, T6501_F03.SB.name(), "更换银行卡未通过", mchnt_txn_ssn);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
        }
        return false;
    }
    
    private void updateT6114Ext(Connection connection, String F04, String F09, String mchnt_txn_ssn)
        throws Throwable
    {
        try (PreparedStatement psmt =
            connection.prepareStatement("UPDATE S61.T6114_EXT SET F04 = ?, F07 = CURRENT_TIMESTAMP(), F09 = ? WHERE F08 = ? "))
        {
            psmt.setString(1, F04);
            psmt.setString(2, F09);
            psmt.setString(3, mchnt_txn_ssn);
            psmt.execute();
        }
    }
    
    private void updateT6114(Connection connection, String F08, String mchnt_txn_ssn)
        throws Throwable
    {
        try (PreparedStatement psmt =
            connection.prepareStatement("UPDATE S61.T6114 SET F08 = ? WHERE F01 = (SELECT F02 FROM S61.T6114_EXT WHERE F08 = ?)"))
        {
            psmt.setString(1, F08);
            psmt.setString(2, mchnt_txn_ssn);
            psmt.execute();
        }
    }
}
