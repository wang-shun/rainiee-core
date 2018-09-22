package com.dimeng.p2p.escrow.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.escrow.fuyou.cond.ChargeCond;
import com.dimeng.p2p.escrow.fuyou.entity.UserQueryEntity;
import com.dimeng.p2p.escrow.fuyou.entity.entities.Auth;
import com.dimeng.p2p.escrow.fuyou.entity.enums.FuyouTypeEnum;
import com.dimeng.p2p.escrow.fuyou.service.ChargeManage;
import com.dimeng.p2p.escrow.fuyou.util.BackCodeInfo;
import com.dimeng.p2p.escrow.fuyou.util.MchntTxnSsn;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.google.gson.Gson;

/**
 * 
 * 充值实现类
 * <富友托管>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年11月10日]
 */
public class ChargeManageImpl extends AbstractEscrowService implements ChargeManage {
    
    public ChargeManageImpl(ServiceResource serviceResource) {
        super(serviceResource);
    }
    
    @Override
    public String addOrder(BigDecimal amount,String source, BigDecimal fee,String payType) throws Throwable {
        if (serviceResource.getSession() == null || !serviceResource.getSession().isAuthenticated()) {
            throw new ParameterException("鉴权失败!");
        }
        String mPhone = configureProvider.getProperty(PayVariavle.CHARGE_MUST_PHONE);
        String mRealName = configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
        // 认证信息
        Auth auth = getAutnInfo();
        if ("S".equals(mPhone) && !auth.phone) {
            throw new LogicalException("手机号未认证!");
        }
        if ("S".equals(mRealName) && !auth.realName) {
            throw new LogicalException("未实名认证!");
        }
        if (amount.compareTo(new BigDecimal(0)) <= 0) {
            throw new ParameterException("金额或支付类型错误!");
        }
        String min = configureProvider.getProperty(PayVariavle.CHARGE_MIN_AMOUNT);
        String max = configureProvider.getProperty(PayVariavle.CHARGE_MAX_AMOUNT);
        if (amount.compareTo(new BigDecimal(min)) < 0 || amount.compareTo(new BigDecimal(max)) > 0) {
            StringBuilder builder = new StringBuilder("充值金额必须大于");
            builder.append(min);
            builder.append("元小于");
            builder.append(max);
            builder.append("元!");
            throw new LogicalException(builder.toString());
        }
        // 数据连接
        try (Connection connection = getConnection()) {
            String mchnt_txn_ssn = MchntTxnSsn.getMts(FuyouTypeEnum.YHCZ.name());
            try {
                // 开启事务
                serviceResource.openTransactions(connection);
                Timestamp time = getCurrentTimestamp(connection);
                // 平台信息
                int pid = getPTID(connection);
                
                T6501 t6501 = new T6501();   // 订单表
                t6501.F02 = OrderType.CHARGE.orderType();  // 类型编码
                t6501.F03 = T6501_F03.DTJ;   // 状态,DTJ:待提交;YTJ:已提交;DQR:待确认;CG:成功;SB:失败;
                t6501.F04 = time;            // 创建时间
                t6501.F05 = time;
                t6501.F07 = T6501_F07.YH;    // 订单来源,XT:系统;YH:用户;HT:后台;
                t6501.F08 = serviceResource.getSession().getAccountId(); // 用户ID
                t6501.F09 = pid;             // 平台ID
                t6501.F10 = mchnt_txn_ssn;   // 流水号
                t6501.F13 = amount;
                int oId = 0;
                
                // 插入订单信息
                oId = insertT6501(connection, t6501);                
                // 充值订单表
                T6502 t6502 = new T6502();
                t6502.F01 = oId;
                t6502.F02 = serviceResource.getSession().getAccountId();                
                t6502.F03 = amount;  // 充值金额                
                t6502.F04 = fee;  // 应收手续费
                t6502.F05 = "ON".equals(configureProvider.format(FuyouVariable.FUYOU_CHAREFEE_ONOFF)) ? fee : BigDecimal.ZERO;  // 实收手续费                
                t6502.F07 = PaymentInstitution.FUYOU.getInstitutionCode(); // 支付公司代号
                t6502.F11 = source;
                t6502.F12 = payType;
                // 插入充值订单信息
                insertT6502(connection, t6502);
                // 提交事务
                serviceResource.commit(connection);
            } catch (Exception e) {
                serviceResource.rollback(connection);
                throw new LogicalException("数据库异常!");
            }            
            return mchnt_txn_ssn;
        }
    }
    
    protected void insertT6502(Connection connection, T6502 entity) throws SQLException  {
        try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S65.T6502 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F09 = ?, F10 = ?,F11=?,F12=? ")) {
            pstmt.setInt(1, entity.F01);
            pstmt.setInt(2, entity.F02);
            pstmt.setBigDecimal(3, entity.F03);
            pstmt.setBigDecimal(4, entity.F04);
            pstmt.setBigDecimal(5, entity.F05);
            pstmt.setString(6, entity.F06);
            pstmt.setInt(7, entity.F07);
            Map<String, Object> result = getEmpInfo(entity.F02, connection);
            pstmt.setString(8, (String)result.get("empNum"));
            pstmt.setString(9, (String)result.get("empStatus"));
            pstmt.setString(10, entity.F11);
            pstmt.setString(11, entity.F12);
            pstmt.execute();
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
    public Auth getAutnInfo()
        throws Throwable
    {
        try (Connection connection = getConnection("S61"))
        {
            Auth auth = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F02, F03, F05 FROM S61.T6118 WHERE T6118.F01 = ? LIMIT 1"))
            {
                pstmt.setInt(1, serviceResource.getSession().getAccountId());
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        auth = new Auth();
                        auth.realName = T6118_F02.TG.name().equals(resultSet.getString(1));
                        auth.phone = T6118_F03.TG.name().equals(resultSet.getString(2));
                    }
                }
            }
            return auth;
        }
    }
    
    @Override
    public Map<String, String> createChargeUrI(String loginId, String amt, String mchntTxnSsn)
        throws Throwable {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mchnt_cd", trimBlank(configureProvider.format(FuyouVariable.FUYOU_ACCOUNT_ID)));
        params.put("mchnt_txn_ssn", mchntTxnSsn);
        params.put("login_id", loginId);
        params.put("amt", amt);
        params.put("page_notify_url", trimBlank(configureProvider.format(FuyouVariable.FUYOU_CHARGE)));
        params.put("back_notify_url", trimBlank(configureProvider.format(FuyouVariable.FUYOU_CHARGENOTICE)));
        logger.info("充值信息==" + new Gson().toJson(params));
        String str = getSignature(params);
        logger.info("充值信息拼接=" + str);
        // 获取签名字符串
        String signature = encryptByRSA(str);
        logger.info("签名=" + signature);
        params.put("signature", signature);
        return params;
    }
    
    @Override
    public Map<String, String> createChargeUrI(ChargeCond cond, BigDecimal fee)
        throws Throwable
    {
        Map<String, String> params = new HashMap<String, String>();
        params.put("mchnt_cd", cond.mchntCd());
        params.put("mchnt_txn_ssn", cond.mchntTxnSsn());
        params.put("login_id", cond.loginId());
        params.put("amt", cond.amt());
        params.put("page_notify_url", trimBlank(cond.pageNotifyUrl()));
        //        params.put("page_notify_url", trimBlank("http://112.95.233.249:5162/app/pay/service/fuyou/ret/chargeRet.htm"));
        //        params.put("page_notify_url", trimBlank("http://www.dimengwx.cc/40080/pay/service/fuyou/ret/chargeRet.htm"));
        params.put("back_notify_url", trimBlank(cond.backNotifyUrl()));
        //        params.put("back_notify_url", trimBlank("http://www.dimengwx.cc/40080/pay/service/fuyou/ret/chargeNotice.htm"));
        //        params.put("back_notify_url",
        //            trimBlank("http://112.95.233.249:5162/app/pay/service/fuyou/ret/chargeNotice.htm"));
        logger.info("充值信息==" + new Gson().toJson(params));
        String str = getSignature(params);
        logger.info("充值信息拼接=" + str);
        // 获取签名字符串
        String signature = encryptByRSA(str);
        logger.info("签名=" + signature);
        params.put("signature", signature);
        return params;
    }
    
    @Override
    public Map<String, String> selectT6501Success(String mchnt_txn_ssn)
        throws Throwable
    {
        Map<String, String> params = null;
        try (Connection connection = getConnection())
        {
            // 查询 订单ID
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F03 FROM S65.T6501 WHERE T6501.F10 = ? LIMIT 1 "))
            {
                pstmt.setString(1, mchnt_txn_ssn);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        params = new HashMap<String, String>();
                        params.put("orderId", String.valueOf(resultSet.getInt(1)));
                        params.put("state", resultSet.getString(2));
                        return params;
                    }
                }
            }
            return params;
        }
    }
        
    @Override
    public void updateT6501(int orderId)
        throws Throwable
    {
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6501 SET F03 = ? , F06 = CURRENT_TIMESTAMP() WHERE F01 = ?"))
            {
                pstmt.setString(1, T6501_F03.SB.toString());
                pstmt.setInt(2, orderId);
                pstmt.execute();
            }
        }
    }
    
    @Override
    public int selectT6311(Connection connection, int accountId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F02 FROM S63.T6311 WHERE F03 = ? FOR UPDATE"))
        {
            pstmt.setInt(1, accountId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                else
                {
                    return 0;
                }
            }
        }
    }
    
    @Override
    public int selectChargeCount(Connection connection, int accountId)
        throws Throwable
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT COUNT(T6501.F01) FROM S65.T6501 INNER JOIN S65.T6502 ON T6501.F01 = T6502.F01 WHERE T6501.F03 = ? AND T6501.F02 = ? AND T6502.F02 = ? "))
        {
            pstmt.setString(1, T6501_F03.CG.name());
            pstmt.setInt(2, OrderType.CHARGE.orderType());
            pstmt.setInt(3, accountId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
            }
        }
        return 0;
    }
    
    @Override
    public void updateT6311(Connection connection, BigDecimal amt, Timestamp t, int accountId)
        throws Throwable
    {
        try (PreparedStatement psc = connection.prepareStatement("UPDATE S63.T6311 SET F04 = ?, F06 = ? WHERE F03 = ?"))
        {
            psc.setBigDecimal(1, amt);
            psc.setTimestamp(2, t);
            psc.setInt(3, accountId);
            psc.execute();
        }
    }
    
    @Override
    public int selectUpperLimit(Connection connection, int exid)
        throws Throwable
    {
        try (PreparedStatement ps =
            connection.prepareStatement("SELECT COUNT(F02) FROM S63.T6311 WHERE F06 >= ? AND F06 <= ? AND F02=? AND F05>0 "))
        {
            Calendar monthCal = Calendar.getInstance();
            monthCal.setTime(new Date());
            monthCal.set(Calendar.DATE, monthCal.getActualMinimum(Calendar.DATE));
            monthCal.set(Calendar.HOUR_OF_DAY, 0);
            monthCal.set(Calendar.MINUTE, 0);
            monthCal.set(Calendar.SECOND, 0);
            ps.setDate(1, new java.sql.Date(monthCal.getTimeInMillis()));
            
            monthCal.set(Calendar.DATE, monthCal.getActualMaximum(Calendar.DATE));
            monthCal.set(Calendar.HOUR_OF_DAY, 23);
            monthCal.set(Calendar.MINUTE, 59);
            monthCal.set(Calendar.SECOND, 59);
            ps.setDate(2, new java.sql.Date(monthCal.getTimeInMillis()));
            
            ps.setInt(3, exid);
            try (ResultSet resultSet = ps.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getInt(1);
                }
                return 0;
            }
        }
    }
    
    @Override
    public boolean chargeRetDecode(Map<String, String> params,T6502 t6502) throws Exception {
        Map<String, String> paramMap = null;   
        
        if (params != null) {
            paramMap = new HashMap<String, String>();
            //交易金额
            paramMap.put("amt", params.get("amt"));
            //交易用户
            paramMap.put("login_id", params.get("login_id"));
            //商户代码
            paramMap.put("mchnt_cd", params.get("mchnt_cd"));
            //请求流水号
            paramMap.put("mchnt_txn_ssn", params.get("mchnt_txn_ssn"));
            //网银充值验签中包含 rem（备注）字段
            if (params.containsKey("rem")) {
                paramMap.put("rem", params.get("rem"));
            }
            //响应码
            paramMap.put("resp_code", params.get("resp_code"));
            // 如果是PC个人快捷充值接口充值回调，验签要用resp_desc, 其它的500001,500002暂不用
            if(t6502.F12.equals("500405")){
	            //响应消息   新版本接口已经不需要描述信息 ,不放到验签 中
	            if (params.containsKey("resp_desc")) {
	            	 paramMap.put("resp_desc", params.get("resp_desc"));
	            }   
            }
            //签名数据
            paramMap.put("signature", params.get("signature"));
        }
        
        //logger.info("充值返回-验签信息paramMap==" + paramMap);
        String str = getSignature(paramMap);
        logger.info("充值返回-验签信息 str==" + str);
        if (!verifyByRSA(str, params.get("signature"))) {
            logger.info("充值-验签失败-返回信息==" + BackCodeInfo.info(params.get("resp_code")));
            return false;
        }
        return true;
    }
    
    @Override
    public void selectT6310(Connection connection, int exid)
        throws Throwable
    {
        try (PreparedStatement psc = connection.prepareStatement("SELECT F01 FROM S63.T6310 WHERE F01=? FOR UPDATE "))
        {
            psc.setInt(1, exid);
            psc.execute();
        }
    }
    
    @Override
    public T6502 selectT6502(Connection connection, int orderId)
        throws SQLException
    {
        T6502 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S65.T6502 WHERE T6502.F01 = ? "))
        {
            pstmt.setInt(1, orderId);
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    record = new T6502();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getString(8);
                }
            }
        }
        return record;
    }
    
    
	@Override
	public T6502 selectT6502BySsn(String mchnt_txn_ssn) throws Throwable {
		 T6502 record = null;
		 try (Connection connection = getConnection())
	        {
		         try (PreparedStatement pstmt =
		             connection.prepareStatement("SELECT T6502.F01, T6502.F02, T6502.F03, T6502.F04, T6502.F05, T6502.F06, T6502.F07, T6502.F08,T6502.F09,T6502.F11,T6502.F12 FROM S65.T6502,S65.T6501 "
		             		+ "WHERE T6502.F01=T6501.F01 AND T6501.F02= ? AND T6501.F10=? "))
		         {
		        	 pstmt.setInt(1, OrderType.CHARGE.orderType());
		             pstmt.setString(2, mchnt_txn_ssn);
		             try (ResultSet resultSet = pstmt.executeQuery())
		             {
		                 if (resultSet.next())
		                 {
		                     record = new T6502();
		                     record.F01 = resultSet.getInt(1);
		                     record.F02 = resultSet.getInt(2);
		                     record.F03 = resultSet.getBigDecimal(3);
		                     record.F04 = resultSet.getBigDecimal(4);
		                     record.F05 = resultSet.getBigDecimal(5);
		                     record.F06 = resultSet.getString(6);
		                     record.F07 = resultSet.getInt(7);
		                     record.F08 = resultSet.getString(8);
		                     record.F09 = resultSet.getString(9);
		                     //record.F10 = resultSet.getString(8);
		                     record.F11 = resultSet.getString("F11");
		                     record.F12 = resultSet.getString("F12");
		                 }
		             }
		         }
	        }
         return record;
	}
    
}
