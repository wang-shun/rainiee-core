/*package com.dimeng.p2p.app.servlets.pay.service.fuyou.achieve;

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
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.app.servlets.pay.service.fuyou.service.ComManage;
import com.dimeng.p2p.escrow.fuyou.cond.ComRegisterCond;
import com.dimeng.p2p.escrow.fuyou.entity.ComRegisterEntity;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

*//**
 * 
 * 法人注册实现类
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月19日]
 *//*
public class ComManageImpl extends AbstractEscrowService implements ComManage
{
    
    public ComManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public ComRegisterEntity selectLegealInfo()
        throws Throwable
    {
        // 查询法人信息
        ComRegisterEntity record = new ComRegisterEntity();
        try (Connection connection = getConnection())
        {
            StringBuilder stb =
                new StringBuilder(
                    "SELECT T6110.F04, T6110.F05, T6161.F04, T6161.F11, T6161.F13, T6114.F05, T6114.F07, T5020.F04, T5019.F03 FROM S61.T6110");
            stb.append(" LEFT JOIN S61.T6161 ON T6161.F01=T6110.F01");
            stb.append(" LEFT JOIN S61.T6114 ON T6114.F02=T6110.F01 AND T6114.F10='TG'");
            stb.append(" LEFT JOIN S50.T5020 ON T5020.F01=T6114.F03 AND T5020.F03='QY'");
            stb.append(" LEFT JOIN S50.T5019 ON T5019.F01=T6114.F04 AND T5019.F13='QY'");
            stb.append(" where T6110.F01=? ");
            try (PreparedStatement pstmt = connection.prepareStatement(stb.toString()))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        // 手机号
                        record.setMobileNo(resultSet.getString(1));
                        // 邮箱
                        record.setEmail(resultSet.getString(2));
                        // 企业名称
                        record.setCustNm(resultSet.getString(3));
                        // 法人名
                        record.setArtifNm(resultSet.getString(4));
                        // 身份证号
                        record.setCertifId(resultSet.getString(5));
                        if (!StringHelper.isEmpty(record.getCertifId()))
                        {
                            record.setCertifId(StringHelper.decode(record.getCertifId()));
                        }
                        // 开户行
                        record.setBankNm(resultSet.getString(6));
                        // 银行卡号
                        record.setCapAcntNo(resultSet.getString(7));
                        if (!StringHelper.isEmpty(record.getCapAcntNo()))
                        {
                            record.setCapAcntNo(StringHelper.decode(record.getCapAcntNo()));
                        }
                        // 银行行别
                        record.setParentBankId(resultSet.getString(8));
                        // 开户行所在的地区号
                        record.setCityId(resultSet.getString(9));
                        // 用户在商户系统的标志
                        record.setUserIdFrom(String.valueOf(serviceResource.getSession().getAccountId()));
                    }
                }
            }
            return record;
        }
    }
    
    @Override
    public Map<String, String> createRegisterUri(ComRegisterCond cond)
        throws Throwable
    {
        Map<String, String> params = new HashMap<String, String>();
        logger.info("COM-用户：" + cond.userIdFrom() + "-备注：富友托管注册");
        // 企业名称
        params.put("artif_nm", cond.artifNm());
        // 后台返回地址<可以不要，前可以实现一样的效果-且为了避免重复回调-富友技术推荐>
        params.put("back_notify_url", "");
        // 开户行支行名称
        params.put("bank_nm", cond.bankNm());
        // 帐号
        params.put("capAcntNo", cond.capAcntNo());
        // 身份证号码   
        params.put("certif_id", cond.certifId());
        // 开户行地区代码
        params.put("city_id", cond.cityId());
        // 客户姓名
        params.put("cust_nm", cond.custNm());
        // 邮箱地址   
        params.put("email", cond.email());
        // 商户代码
        params.put("mchnt_cd", cond.mchntCd());
        // 流水号 
        params.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        // 手机号码
        params.put("mobile_no", cond.mobileNo());
        // 商户返回地址
        params.put("page_notify_url", cond.pageNotifyUrl());
        // 开户行行别 
        params.put("parent_bank_id", cond.parentBankId());
        // 用户在商户系统的标志
        params.put("user_id_from", cond.userIdFrom());
        // 数据拼接
        String dataStr = getSignature(params);
        logger.info("用户注册信息：" + dataStr);
        String signature = encryptByRSA(dataStr);
        logger.info("签名结果：" + signature);
        params.put("signature", signature);
        return params;
    }
    
    @Override
    public void updateLegealInfo(Map<String, String> retMap)
        throws SQLException, Throwable
    {
        logger.info("企业用户注册返回更新信息！");
        if (retMap == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            
            int userId = IntegerParser.parse(retMap.get("user_id_from"));
            String mobile_no = retMap.get("mobile_no");
            // 防止富友二次返回注册数据 导致sql主键冲突，消耗库连接资源
            boolean flag = selectT6119(connection, userId, mobile_no);
            if (flag)
            {
                logger.info("处理注册时返回数据-重复返回，用户ID：" + userId + " 手机号：" + mobile_no);
                return;
            }
            // 第三方注册后，企业T6161未更改过，所不需要更新
            // 更新T6110 用户信息表，增加邮箱地址
            updateT6110(connection, retMap.get("email"), userId);
            // 更新 T6118 用户安全认证
            updateT6118(connection, userId);
            // 插入第三方托管账号(请注意：在富友那边，是以注册时候的手机号当做用户在富友的登录账户。之后，用户即使修改了手机号，这个账户也不会变。)
            insertT6119(connection, userId, PaymentInstitution.FUYOU.getInstitutionCode(), mobile_no);
            // 添加开户行地区代码、开户行支行名称、帐号、加密帐号
            insertT6114(connection,
                userId,
                retMap.get("parent_bank_id"),
                IntegerParser.parse(retMap.get("city_id")),
                retMap.get("bank_nm"),
                retMap.get("capAcntNo").replace(retMap.get("capAcntNo").substring(4,
                    retMap.get("capAcntNo").length() - 3),
                    getTargetStr(retMap.get("capAcntNo").length() - 7)),
                StringHelper.encode(retMap.get("capAcntNo")),
                retMap.get("artif_nm"));
            
            // 更新T6165 企业账号审核状态
            insertT6165(connection, userId);
            logger.info("企业用户成功注册！");
        }
    }
    
    *//**
     * 更新T6165 企业账号审核状态
     * 
     * @param connection
     * @param F01
     *            用户Id
     * @throws Throwable
     *//*
    private void insertT6165(Connection connection, int F01)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT T6165.F01 FROM S61.T6165 WHERE T6165.F01=?"))
        {
            pstmt.setInt(1, F01);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("UPDATE S61.T6165 SET F02 = 'Y', F04 = ?  WHERE F01 = ? "))
                    {
                        ps.setTimestamp(1, getCurrentTimestamp(connection));
                        ps.setInt(2, F01);
                        ps.execute();
                    }
                    return;
                }
            }
        }
        try (PreparedStatement pstmts =
            connection.prepareStatement("INSERT INTO S61.T6165 SET F01 = ?, F02 = 'Y', F04 = ? "))
        {
            pstmts.setInt(1, F01);
            pstmts.setTimestamp(2, getCurrentTimestamp(connection));
            pstmts.execute();
        }
    }
    
    *//**
     * 更新或创建企业用户T6114 用户银行卡信息
     * 
     * @param connection
     *            数据连接
     * @param F02
     *            用户ID
     * @param F03
     *            开户行行别ID
     * @param F04
     *            开户行所在地区域ID
     * @param F05
     *            开户行支行名称
     * @param F06
     *            银行卡号,前4位,后3位保留,其他星号代替
     * @param F07
     *            银行卡号,加密存储
     * @throws Throwable
     *//*
    private void insertT6114(Connection connection, int F02, String F03, int F04, String F05, String F06, String F07,
        String name)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT T6114.F01 FROM S61.T6114 WHERE T6114.F02=?"))
        {
            pstmt.setInt(1, F02);
            try (ResultSet rs = pstmt.executeQuery())
            {
                if (rs.next())
                {
                    try (PreparedStatement ps =
                        connection.prepareStatement("UPDATE S61.T6114 SET F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = 'QY', F10 = 'TG', F11= ? , F12 = 2  WHERE F01 = ? "))
                    {
                        ps.setInt(1, Integer.valueOf(F03));
                        ps.setInt(2, F04);
                        ps.setString(3, F05);
                        ps.setString(4, F06);
                        ps.setString(5, F07);
                        ps.setString(6, name);
                        ps.execute();
                    }
                    return;
                }
            }
        }
        try (PreparedStatement pstmts =
            connection.prepareStatement("INSERT INTO S61.T6114 SET F02 = ?, "
                + "F03 = (SELECT F01 FROM S50.T5020 WHERE F04 = ? LIMIT 1), " + "F04 = ? , F05 = ?, F06 = ?, "
                + "F07 = ?, F08 = 'QY', F09 = ?, F10 = 'TG',F11= ?,F12= 2"))
        {
            pstmts.setInt(1, F02);
            pstmts.setString(2, F03);
            pstmts.setInt(3, F04);
            pstmts.setString(4, F05);
            pstmts.setString(5, F06);
            pstmts.setString(6, F07);
            pstmts.setTimestamp(7, getCurrentTimestamp(connection));
            pstmts.setString(8, name);
            pstmts.execute();
        }
    }
    
    *//**
     * 获取一个*序列
     * 
     * @param length
     * @return
     *//*
    public String getTargetStr(int length)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            sb.append("*");
        }
        return sb.toString();
    }
    
    *//**
     * 插入企业用户第三方托管账号
     * 
     * @param connection
     *            数据连接
     * @param userId
     *            用户ID
     * @param institutionCode
     *            第三方ID
     * @param mobileNo
     *            托管账号ID 即手机号
     * @throws SQLException
     *//*
    private void insertT6119(Connection connection, int userId, int institutionCode, String mobileNo)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6119 SET F01 = ?, F02 = ?, F03 = ?"))
        {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, institutionCode);
            pstmt.setString(3, mobileNo);
            pstmt.execute();
        }
    }
    
    *//**
     * 企业用户增加邮箱地址
     * 
     * @param connection
     * @param email
     * @throws SQLException
     *//*
    private void updateT6110(Connection connection, String email, int F01)
        throws SQLException
    {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE S61.T6110 SET F05 = ?  WHERE F01 = ? "))
        {
            ps.setString(1, email);
            ps.setInt(2, F01);
            ps.execute();
        }
    }
    
    *//**
     * 更新 T6118 用户安全认证
     * 
     * @param connection
     * @param tg
     *            状态通过
     * @param userId
     *            用户ID
     * @throws SQLException
     *//*
    private void updateT6118(Connection connection, int userId)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6118 SET F02 = 'TG' WHERE F01 = ?"))
        {
            pstmt.setInt(1, userId);
            pstmt.execute();
        }
    }
    
    *//**
     * 查询否是存在托管账号
     * 
     * @param connection
     * @param F09
     *            用户手机号（富友托管登录账号）
     * @return
     * @throws SQLException
     *//*
    private boolean selectT6119(Connection connection, int F01, String F03)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01 FROM S61.T6119 WHERE T6119.F01 = ? AND T6119.F03 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setString(2, F03);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    logger.info("处理注册时返回数据-重复返回，用户ID：" + F01 + " 手机号：" + F03);
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public boolean selectT6119(String F03)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("SELECT F01 FROM S61.T6119 WHERE T6119.F03 = ?"))
            {
                pstmt.setString(1, F03);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        logger.info("处理注册时返回数据-注册异常处理- 手机号：" + F03 + "备注：此手机号已被占用");
                        return false;
                    }
                }
            }
            return true;
        }
    }
    
    @Override
    public UserQueryEntity userChargeQuery(String MCHNT_CD, String user_ids)
        throws Throwable
    {
        *//**
         * 加载用户信息查询-富友
         *//*
        UserQueryEntity userQueryEntity = new UserQueryEntity();
        // 商户代码
        userQueryEntity.setMchnt_cd(MCHNT_CD);
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
    public boolean registerReturnDecoder(Map<String, String> params)
        throws Throwable
    {
        return getReturnParams(params);
    }
}
*/