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
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F04;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S61.enums.T6141_F09;
import com.dimeng.p2p.escrow.fuyou.cond.UserRegisterCond;
import com.dimeng.p2p.escrow.fuyou.entity.OpenEscrowGuideEntity;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryResponseEntity;
import com.dimeng.p2p.escrow.fuyou.entity.UserRegisterEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.UserManage;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 
 * 用户管理实现类 <功能详细描述>
 * 
 * @author heshiping
 * @version [版本号, 2015年11月10日]
 */
public class UserManageImpl extends AbstractEscrowService implements UserManage
{
    public UserManageImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    @Override
    public boolean isBandPhoneAndEmail()
        throws Throwable
    {
        // 是否绑定实名认证和邮箱
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F03, F04 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        String bandPhone = resultSet.getString(1);
                        String bandEmail = resultSet.getString(2);
                        if (T6118_F03.TG.name().equals(bandPhone) && T6118_F04.TG.name().equals(bandEmail))
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
    public UserRegisterEntity selectUserInfo()
        throws Throwable
    {
        UserRegisterEntity entity = null;
        try (Connection connection = getConnection())
        {
            StringBuilder sb =
                new StringBuilder(
                    "select T6110.F04,T6110.F05,T6141.F02,T6141.F07,T6114.F05,T6114.F07,T5020.F04,T5019.F03 from S61.T6110");
            sb.append(" LEFT JOIN S61.T6141 ON T6141.F01=T6110.F01 AND T6141.F04='TG'");
            sb.append(" LEFT JOIN S61.T6114 ON T6114.F02=T6110.F01 AND T6114.F10='TG' AND T6114.F08='QY'");
            sb.append(" LEFT JOIN S50.T5020 ON T5020.F01=T6114.F03");
            sb.append(" LEFT JOIN S50.T5019 ON T5019.F01=T6114.F04 where T6110.F01=?");
            try (PreparedStatement pstmt = connection.prepareStatement(sb.toString()))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        entity = new UserRegisterEntity();
                        // 手机号
                        entity.setMobile_no(resultSet.getString(1));
                        // 邮箱
                        entity.setEmail(resultSet.getString(2));
                        // 姓名
                        entity.setCust_nm(resultSet.getString(3));
                        // 身份证号
                        String certif_id = resultSet.getString(4);
                        if (!StringHelper.isEmpty(certif_id))
                        {
                            entity.setCertif_id(StringHelper.decode(certif_id));
                        }
                        else
                        {
                            entity.setCertif_id("");
                        }
                        // 支行名称
                        entity.setBank_nm(resultSet.getString(5));
                        // 银行卡号
                        String capAcntNo = resultSet.getString(6);
                        if (!StringHelper.isEmpty(capAcntNo))
                        {
                            entity.setCapAcntNo(StringHelper.decode(capAcntNo));
                        }
                        else
                        {
                            entity.setCapAcntNo("");
                        }
                        // 银行行别
                        entity.setParent_bank_id(resultSet.getString(7));
                        // 开户行所在的地区号
                        entity.setCity_id(resultSet.getString(8));
                        // 用户的iD
                        entity.setUser_id_from(String.valueOf(serviceResource.getSession().getAccountId()));
                    }
                }
                if (selectAccountNO(connection, entity.getMobile_no()))
                {
                    throw new LogicalException("您使用的手机号已被占用，请更换手机号！");
                }
            }
        }
        return entity;
    }
    
    /**
     * 查询此手机号是否已被占用
     * @param mobileNo
     * @return
     * @throws Throwable
     */
    private boolean selectAccountNO(Connection connection, String mobileNo)
        throws Throwable
    {
        try (PreparedStatement preparedStatement =
            connection.prepareStatement("SELECT F01 FROM S61.T6119 WHERE T6119.F03 = ? "))
        {
            preparedStatement.setString(1, mobileNo);
            try (ResultSet resultSet = preparedStatement.executeQuery())
            {
                if (resultSet.next())
                {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public Map<String, String> createRegisterUri(UserRegisterCond cond)
        throws Throwable
    {
        Map<String, String> params = new HashMap<String, String>();
        logger.info("个人用户：" + cond.userIdFrom() + "-备注：富友托管注册");
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
    public boolean registerReturnDecoder(Map<String, String> params)
        throws Throwable
    {
        Map<String, String> paramMap = null;
        
        if (params != null && params.size() > 0)
        {
            paramMap = new HashMap<String, String>();
            
            //开户行支行名称
            paramMap.put("bank_nm", params.get("bank_nm"));
            //帐号
            paramMap.put("capAcntNo", params.get("capAcntNo"));
            //身份证号码
            paramMap.put("certif_id", params.get("certif_id"));
            //开户行地区代码
            paramMap.put("city_id", params.get("city_id"));
            //客户姓名
            paramMap.put("cust_nm", params.get("cust_nm"));
            //邮箱地址
            paramMap.put("email", params.get("email"));
            //商户代码
            paramMap.put("mchnt_cd", params.get("mchnt_cd"));
            //请求流水号
            paramMap.put("mchnt_txn_ssn", params.get("mchnt_txn_ssn"));
            //手机号码
            paramMap.put("mobile_no", params.get("mobile_no"));
            //开户行行别
            paramMap.put("parent_bank_id", params.get("parent_bank_id"));
            //响应码
            paramMap.put("resp_code", params.get("resp_code"));
            //用户在商户系统的标志
            paramMap.put("user_id_from", params.get("user_id_from"));
            //签名数据
            paramMap.put("signature", params.get("signature"));
        }
        
        //返回验签结果
        return getReturnParams(paramMap);
    }
    
    @Override
    public void updateUserInfo(Map<String, String> retMap)
        throws Throwable
    {
        if (retMap == null)
        {
            return;
        }
        try (Connection connection = getConnection())
        {
            try
            {
                int userId = IntegerParser.parse(retMap.get("user_id_from"));
                // 手机号
                String mobile_no = retMap.get("mobile_no");
                // 防止富友二次返回注册数据 导致sql主键冲突，消耗库连接资源
                boolean flag = selectT6119(connection, userId, mobile_no);
                if (flag)
                {
                    logger.info("处理注册时返回数据-重复返回，用户ID：" + userId + " 手机号：" + mobile_no);
                    return;
                }
                serviceResource.openTransactions(connection);
                updateT6110(connection, userId, retMap.get("mobile_no"), retMap.get("email"));
                // 更新身份证、真实姓名
                updateT6141(connection, retMap.get("cust_nm"), retMap.get("certif_id"), retMap.get("certif_id"), userId);
                // 更新身份
                updateT6118(connection,
                    userId,
                    T6118_F02.TG,
                    T6118_F03.TG,
                    retMap.get("mobile_no"),
                    retMap.get("email"));
                // 插入第三方托管账号(请注意：在富友那边，是以注册时候的手机号当做用户在富友的登录账户。之后，用户即使修改了手机号，这个账户也不会变。)
                insertT6119(connection, userId, PaymentInstitution.FUYOU.getInstitutionCode(), mobile_no);
                // 添加开户行地区代码、开户行支行名称、帐号、加密帐号
                insertT6114(connection,
                    userId,	
                    retMap.get("parent_bank_id"),
                    IntegerParser.parse(retMap.get("city_id")),
                    retMap.get("bank_nm"),
                    retMap.get("capAcntNo").replace(retMap.get("capAcntNo").substring(4,
                        retMap.get("capAcntNo").length() - 4),
                        getTargetStr(retMap.get("capAcntNo").length() - 7)),
                    StringHelper.encode(retMap.get("capAcntNo")),
                    retMap.get("cust_nm"));
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
            	logger.error("",e);
                serviceResource.rollback(connection);
                throw e;
            }
        }
        
    }
    
    /**
     * 更新手机号、邮箱
     * 
     * @param connection
     * @param F01
     * @param F04
     * @param F05
     * @throws Throwable
     */
    protected void updateT6110(Connection connection, int F01, String F04, String F05)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6110 SET F04 = ?, F05 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F04);
            pstmt.setString(2, F05);
            pstmt.setInt(3, F01);
            pstmt.execute();
        }
    }
    
    /**
     * 更新身份证、真实姓名
     * 
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @param F04
     * @throws Throwable
     */
    protected void updateT6141(Connection connection, String F01, String F02, String F03, int F04)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6141 SET F02 = ?, F06 = ?, F07 = ?, F04 = ?,F08 = ?, F09 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01);
            pstmt.setString(2, StringHelper.truncation(F02, 3, "***************"));
            pstmt.setString(3, StringHelper.encode(F03));
            pstmt.setString(4, T6141_F04.TG.name());
            Timestamp date = getBirthday(F03);
            pstmt.setTimestamp(5, date);
            pstmt.setString(6, getSexNotDecode(F02));
            pstmt.setInt(7, F04);
            pstmt.execute();
        }
    }
    
    private String getSexNotDecode(String sfzh)
    {
        if (StringHelper.isEmpty(sfzh))
        {
            return null;
        }
        String sex = null;
        try
        {
            if (Integer.parseInt(sfzh.substring(sfzh.length() - 2, sfzh.length() - 1)) % 2 != 0)
            {
                sex = T6141_F09.M.name();
            }
            else
            {
                sex = T6141_F09.F.name();
            }
        }
        catch (Throwable throwable)
        {
            logger.error("UserManageImpl.getSexNotDecode error", throwable);
        }
        return sex;
    }
    
    /**
     * 根据身份证得到出生年月
     * 
     * @param cardID
     * @return
     */
    private static Timestamp getBirthday(String cardID)
    {
        StringBuffer tempStr = new StringBuffer("");
        if (cardID != null && cardID.trim().length() > 0)
        {
            if (cardID.trim().length() == 15)
            {
                tempStr.append(cardID.substring(6, 12));
                tempStr.insert(4, '-');
                tempStr.insert(2, '-');
                tempStr.insert(0, "19");
            }
            else if (cardID.trim().length() == 18)
            {
                tempStr = new StringBuffer(cardID.substring(6, 14));
                tempStr.insert(6, '-');
                tempStr.insert(4, '-');
            }
        }
        return TimestampParser.parse(tempStr.toString());
    }
    
    /**
     * 更新身份认证状态
     * 
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @param F06
     * @param F07
     * @throws SQLException
     */
    protected void updateT6118(Connection connection, int F01, T6118_F02 F02, T6118_F03 F03, String F06, String F07)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6118 SET F02 = ?, F03 = ?, F04 = ?, F06 = ?, F07 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F02.name());
            pstmt.setString(2, F03.name());
            pstmt.setString(3, "".equals(F07) ? T6118_F04.BTG.name() : T6118_F04.TG.name());
            pstmt.setString(4, F06);
            pstmt.setString(5, F07);
            pstmt.setInt(6, F01);
            pstmt.execute();
        }
    }
    
    /**
     * 获取一个*序列
     * 
     * @param length
     * @return
     */
    public String getTargetStr(int length)
    {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            sb.append("*");
        }
        return sb.toString();
    }
    
    /**
     * 插入第三方托管账号
     * 
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @throws SQLException
     */
    protected void insertT6119(Connection connection, int F01, int F02, String F03)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6119 SET F01 = ?, F02 = ?, F03 = ?"))
        {
            pstmt.setInt(1, F01);
            pstmt.setInt(2, F02);
            pstmt.setString(3, F03);
            pstmt.execute();
        }
    }
    
    /**
     * 插入开户行地区代码、开户行支行名称、帐号、加密帐号
     * 
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @param F04
     * @param F05
     * @throws SQLException
     */
    protected void insertT6114(Connection connection, int F02, String F03, int F04, String F05, String F06, String F07,
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
                    return;
                }
            }
        }
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S61.T6114 SET F02 = ?, "
                + "F03 = (SELECT F01 FROM S50.T5020 WHERE F04 = ? LIMIT 1), "
                + "F04 = (SELECT F01 FROM S50.T5019 WHERE F03 = ? LIMIT 1), F05 = ?, F06 = ?, "
                + "F07 = ?, F08 = 'QY', F09 = CURRENT_TIMESTAMP(), F10 = 'TG', F11=?, F12= 1"))
        {
            pstmt.setInt(1, F02);
            pstmt.setString(2, F03);
            pstmt.setInt(3, F04);
            pstmt.setString(4, F05);
            pstmt.setString(5, F06);
            pstmt.setString(6, F07);
            pstmt.setString(7, name);
            pstmt.execute();
        }
    }
    
    @Override
    public void queryUserInfo(UserQueryEntity entity)
        throws Throwable
    {
        logger.info("UserManageImpl.reviseUserInfo() start...");
        // 商户代码
        String mchnt_cd = entity.getMchnt_cd();
        // 流水号
        String mchnt_txn_ssn = entity.getMchnt_txn_ssn();
        // 交易日期
        String mchnt_txn_dt = entity.getMchnt_txn_dt();
        // 待查询的登录帐户列表
        String user_ids = entity.getUser_ids();
        logger.info("商户查询交易日期：mchnt_txn_dt=" + mchnt_txn_dt + ",查询企业注册的手机号:user_ids=" + user_ids + ",商户代码:mchnt_cd="
            + mchnt_cd + ",流水号:mchnt_txn_ssn=" + mchnt_txn_ssn);
        Map<String, String> params = new HashMap<String, String>();
        params.put("mchnt_cd", mchnt_cd);
        params.put("mchnt_txn_dt", mchnt_txn_dt);
        params.put("mchnt_txn_ssn", mchnt_txn_ssn);
        params.put("user_ids", user_ids);
        String stc = getSignature(params);
        logger.info("拼接 = " + stc);
        String signature = encryptByRSA(stc);
        logger.info("UserManageImpl.reviseUserInfo() end...");
        entity.setSignature(signature);
    }
    
    @Override
    public UserQueryResponseEntity userQueryReturnDecoder(HashMap<String, Object> xmlMap, String plain)
        throws Throwable
    {
        logger.info("UserManageImpl.userQueryReturnDecoder() start...");
        logger.info("返回参数值：plain = " + plain);
        logger.info("返回参数值：signature = " + xmlMap.get("signature"));
        
        boolean verifyResult = verifyByRSA(plain, (String)(xmlMap.get("signature")));
        logger.info("验签结果：" + verifyResult);
        
        if (verifyResult)
        {
            UserQueryResponseEntity entity = new UserQueryResponseEntity();
            // 响应码
            entity.setResp_code((String)(xmlMap.get("resp_code")));
            // 商户代码
            entity.setMchnt_cd((String)(xmlMap.get("mchnt_cd")));
            // 请求流水号
            entity.setMchnt_txn_ssn((String)(xmlMap.get("mchnt_txn_ssn")));
            // 签名数据
            entity.setSignature((String)(xmlMap.get("signature")));
            // 客户姓名
            entity.setCust_nm((String)(xmlMap.get("cust_nm")));
            // 身份证号
            entity.setCertif_id((String)(xmlMap.get("certif_id")));
            // 手机号
            entity.setMobile_no((String)(xmlMap.get("mobile_no")));
            // 邮箱地址
            entity.setEmail((String)(xmlMap.get("email")));
            // 开户行地区代码
            entity.setCity_id((String)(xmlMap.get("city_id")));
            // 开户行行别
            entity.setParent_bank_id((String)(xmlMap.get("parent_bank_id")));
            // 开户行支行名称
            entity.setBank_nm((String)(xmlMap.get("bank_nm")));
            // 帐号
            entity.setCapAcntNo((String)(xmlMap.get("capAcntNo")));
            // 签约状态
            entity.setCard_pwd_verify_st((String)(xmlMap.get("card_pwd_verify_st")));
            // 账户信息验证状态
            entity.setId_nm_verify_st((String)(xmlMap.get("id_nm_verify_st")));
            // 账户生效状态
            entity.setContract_st((String)(xmlMap.get("contract_st")));
            // 账户生效状态
            entity.setUser_st((String)(xmlMap.get("user_st")));
            
            logger.info("UserManageImpl.userReviseReturnDecoder() end..." + entity);
            return entity;
        }
        else
        {
            logger.info("UserManageImpl.userQueryReturnDecoder() start...NULL");
            return null;
        }
    }
    
    @Override
    public UserQueryEntity userChargeQuery(String mchnt_cd, String user_ids)
        throws Throwable
    {
        /**
         * 加载用户信息查询-富友
         */
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
    public T6110 selectT6110()
        throws Throwable
    {
        T6110 record = null;
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        record = new T6110();
                        record.F01 = resultSet.getInt(1);
                        record.F02 = resultSet.getString(2);
                        record.F03 = resultSet.getString(3);
                        record.F04 = resultSet.getString(4);
                        record.F05 = resultSet.getString(5);
                        record.F06 = T6110_F06.parse(resultSet.getString(6));
                        record.F07 = T6110_F07.parse(resultSet.getString(7));
                        record.F08 = T6110_F08.parse(resultSet.getString(8));
                        record.F09 = resultSet.getTimestamp(9);
                        record.F10 = T6110_F10.parse(resultSet.getString(10));
                    }
                }
            }
        }
        return record;
    }
    
    @Override
    public boolean isZrr()
        throws Throwable
    {
        // 是否是自然人
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F06 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (resultSet.getString(1).equals(T6110_F06.ZRR.name()))
                        {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }
    
    /**
     * 查询否是存在托管账号
     * 
     * @param connection
     * @param F09
     *            用户手机号（富友托管登录账号）
     * @return
     * @throws SQLException
     */
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
    public boolean isSmrz()
        throws Throwable
    {
        // 是否实名认证
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        if (T6118_F02.parse(resultSet.getString(1)) == T6118_F02.TG)
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
    public String selectT6119()
        throws Throwable
    {
        return getAccountId(serviceResource.getSession().getAccountId());
    }
    
    @Override
    public OpenEscrowGuideEntity getOpenEscrowGuideEntity()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            OpenEscrowGuideEntity entity = new OpenEscrowGuideEntity();
            int userId = serviceResource.getSession().getAccountId();
            T6110 t6110 = selectT6110(connection, userId);
            if (t6110 == null)
                return null;
            if (T6110_F06.ZRR.equals(t6110.F06))
            {
                entity.setRealNameLabel("真实姓名");
                entity.setIdentificationNoLabel("身份证号");
                T6141 t6141 = selectT6141(connection);
                entity.setRealName(t6141.F02 == null ? "" : t6141.F02);
                entity.setIdentificationNo(t6141.F07 == null ? "" : StringHelper.decode(t6141.F07));
                entity.setUserTag("ZRR");
                entity.setMaxLength("18");
            }
            else
            {
                String prefix = "";
                if (T6110_F10.S.equals(t6110.F10))
                {
                    prefix = "机构";
                    entity.setUserTag("JG");
                }
                else
                {
                    prefix = "企业";
                    entity.setUserTag("QY");
                }
                entity.setRealNameLabel(prefix + "名称");
                T6161 t6161 = selectT6161(connection);
                entity.setRealName(t6161.F04 == null ? "" : t6161.F04);
                if ("Y".equals(t6161.F20))
                {
                    entity.setIdentificationNoLabel(prefix + "社会信用代码");
                    entity.setIdentificationNo(t6161.F19 == null ? "" : t6161.F19);
                    entity.setMaxLength("18");//社信代码只有18位
                }
                else
                {
                    entity.setIdentificationNoLabel(prefix + "营业执照号码");
                    entity.setIdentificationNo(t6161.F03 == null ? "" : t6161.F03);
                    entity.setMaxLength("20");
                }
            }
            entity.setMobile(t6110.F04 == null ? "" : t6110.F04);
            return entity;
        }
    }
    
    private T6141 selectT6141(Connection connection)
        throws SQLException
    {
        T6141 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6141();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = T6141_F03.parse(resultSet.getString(3));
                    record.F04 = T6141_F04.parse(resultSet.getString(4));
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getString(7);
                    record.F08 = resultSet.getDate(8);
                }
            }
        }
        return record;
    }
    
    private T6161 selectT6161(Connection conn)
        throws Throwable
    {
        T6161 record = null;
        try (PreparedStatement pstmt =
            conn.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F11, F12, F13, F14, F15, F16, F17, F18, F19, F20 FROM S61.T6161 WHERE T6161.F01 = ? LIMIT 1"))
        {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6161();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = resultSet.getString(3);
                    record.F04 = resultSet.getString(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getBigDecimal(8);
                    record.F09 = resultSet.getString(9);
                    record.F10 = resultSet.getInt(10);
                    record.F11 = resultSet.getString(11);
                    record.F12 = resultSet.getString(12);
                    record.F13 = resultSet.getString(13);
                    record.F14 = resultSet.getBigDecimal(14);
                    record.F15 = resultSet.getBigDecimal(15);
                    record.F16 = resultSet.getString(16);
                    record.F17 = resultSet.getString(17);
                    record.F18 = resultSet.getString(18);
                    record.F19 = resultSet.getString(19);
                    record.F20 = resultSet.getString(20);
                }
            }
        }
        return record;
    }
    
    @Override
    public T6110_F06 getUserType()
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F06 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        return T6110_F06.parse(resultSet.getString(1));
                    }
                }
            }
        }
        return null;
    }
}
