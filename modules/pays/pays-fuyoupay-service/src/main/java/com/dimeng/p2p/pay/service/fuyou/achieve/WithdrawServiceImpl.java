package com.dimeng.p2p.pay.service.fuyou.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.config.Envionment;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.FeeCode;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6130;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6110_F18;
import com.dimeng.p2p.S61.enums.T6110_F19;
import com.dimeng.p2p.S61.enums.T6114_F08;
import com.dimeng.p2p.S61.enums.T6123_F05;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.S61.enums.T6130_F16;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S62.enums.T6252_F09;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6503;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.common.SMSUtils;
import com.dimeng.p2p.pay.service.fuyou.WithdrawService;
import com.dimeng.p2p.pay.service.fuyou.entity.Payforreq;
import com.dimeng.p2p.pay.service.fuyou.entity.Qrytransreq;
import com.dimeng.p2p.pay.service.fuyou.util.Bean2XmlUtils;
import com.dimeng.p2p.pay.service.fuyou.util.FuYouMD5;
import com.dimeng.p2p.pay.service.fuyou.varibles.FuyouPayVaribles;
import com.dimeng.p2p.variables.defines.LetterVariable;
import com.dimeng.p2p.variables.defines.MsgVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.p2p.variables.defines.smses.SmsVaribles;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.DateTimeParser;
import com.dimeng.util.parser.IntegerParser;

public class WithdrawServiceImpl extends AbstractPayService implements WithdrawService {

	public WithdrawServiceImpl(ServiceResource serviceResource) {
		super(serviceResource);
	}
	
	/**
	 * 提现
	 * @param withdrawPsd
	 * @param funds
	 * @param cardId
	 * @param poundage
	 * @param txkcfs
	 * @return
	 * @throws Throwable
	 */
    public int withdraw(String withdrawPsd, BigDecimal funds, int cardId, BigDecimal poundage, boolean txkcfs) throws Throwable {
    	ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        BigDecimal min = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_MIN_FUNDS));
        BigDecimal max = new BigDecimal(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_FUNDS));
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (funds.compareTo(min) < 0 || funds.compareTo(max) > 0 || funds.compareTo(BigDecimal.ZERO) <= 0) {
            throw new LogicalException("提现金额不能低于" + min + "元，不能高于" + max + "元");
        }
        int accountId = serviceResource.getSession().getAccountId();
        
        try (Connection connection = getConnection()) {
        	try{
            int count = psdInputCount(connection);
            int maxCount = IntegerParser.parse(configureProvider.getProperty(SystemVariable.WITHDRAW_MAX_INPUT));
            if (count >= maxCount) {
                throw new LogicalException("您今日交易密码输入错误已到最大次数，请改日再试!");
            }
            if (isYuqi(connection, accountId)) {
                throw new LogicalException("您有逾期未还的借款，请先还完再操作。");
            }
            
            boolean aa = false;// 标记交易密码是否正确
            if (isOpenWithPsd) {
                try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S61.T6118 WHERE F01=? AND F08=?")) {
                    ps.setInt(1, accountId);
                    ps.setString(2, UnixCrypt.crypt(withdrawPsd, DigestUtils.sha256Hex(withdrawPsd)));
                    try (ResultSet resultSet = ps.executeQuery()) {
                        if (resultSet.next()) {
                            aa = true;
                        }
                    }
                }
            }
            serviceResource.openTransactions(connection);
            if (!aa && isOpenWithPsd) {
            	execute(connection, "UPDATE S61.T6110 SET F11 = F11+1 WHERE F01 = ?", accountId);
                serviceResource.commit(connection);
                String errorMsg = null;
                if (count + 1 >= maxCount) {
                    errorMsg = "您今日交易密码输入错误已到最大次数，请改日再试!";
                } else {
                    StringBuilder builder = new StringBuilder("交易密码错误,您最多还可以输入");
                    builder.append(maxCount - (count + 1));
                    builder.append("次！");
                    errorMsg = builder.toString();
                }
                throw new LogicalException(errorMsg);
            }
                
            T6114 t6114 = this.selectT6114(connection, cardId, accountId);
            if (null == t6114) {
                throw new LogicalException("银行卡不存在！");
            }
                
            // 提现手续费扣除方式
            BigDecimal amount = BigDecimal.ZERO;// 提现应付金额
            BigDecimal money = new BigDecimal(configureProvider.format(SystemVariable.WITHDRAW_LIMIT_FUNDS));
            if (txkcfs) {
                amount = funds;
                funds = funds.subtract(poundage);
                if (amount.doubleValue() < poundage.doubleValue()) {
                    throw new LogicalException("提现金额不能小于提现手续费！");
                }
            } else {
                amount = funds.add(poundage);
                money = money.add(poundage);
            }              
            
            // 用户往来账户信息
            T6101 wlzh = selectT6101(accountId, T6101_F03.WLZH, true);
            if (wlzh == null)
            {
                throw new LogicalException("用户往来账户不存在");
            }
            
            wlzh.F06 = wlzh.F06.subtract(funds).subtract(poundage);
            
            if (wlzh.F06.compareTo(new BigDecimal(0)) < 0)
            {
                throw new LogicalException("用户账户可用金额不足");
            }
            
            Timestamp timestamp = getCurrentTimestamp(connection);
            
            String bankCode = this.selectT5020(connection, t6114.F03); 
            String cityCode = this.selectT5019(connection, t6114.F04);
            
            int id = this.insertT6130(connection, accountId, cardId, funds, poundage, timestamp, bankCode, cityCode);
        
            T6501 t6501 = new T6501();
            t6501.F02 = OrderType.WITHDRAW.orderType();
            t6501.F03 = T6501_F03.DTJ;
            t6501.F04 = timestamp;
            t6501.F05 = timestamp;
            t6501.F06 = timestamp;
            t6501.F07 = T6501_F07.HT;
            t6501.F08 = accountId;
            t6501.F09 = 0;
            t6501.F13 = funds;
            int orderId = insertT6501(connection, t6501);	        
            try (PreparedStatement pstmt =
            		connection.prepareStatement("INSERT INTO S65.T6503 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?,F09 = ? ")) {
            	pstmt.setInt(1, orderId);
            	pstmt.setInt(2, accountId);
            	pstmt.setBigDecimal(3, funds);
            	pstmt.setBigDecimal(4, poundage);
            	pstmt.setBigDecimal(5, poundage);
            	pstmt.setString(6, t6114.F07);
            	pstmt.setInt(7, id);
            	pstmt.execute();
            }            
            serviceResource.commit(connection);
            return orderId;
        }catch(Exception e){
        	serviceResource.rollback(connection);
     	   throw e;
        }
       
        }
        
    }
    
    protected T6114 selectT6114(Connection connection, int cardId, int userId) throws Throwable  {
        T6114 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F11 FROM S61.T6114 WHERE T6114.F01 = ? AND F02 = ?")){
            pstmt.setInt(1, cardId);
            pstmt.setInt(2, userId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6114();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getInt(4);
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = StringHelper.decode(resultSet.getString(7));
                    record.F08 = T6114_F08.parse(resultSet.getString(8));
                }
            }
        }
        return record;
    }
    
    protected String selectT5020(Connection connection, int bankId) throws Throwable {
		try (PreparedStatement pstmt = connection.prepareStatement("SELECT F05 FROM S50.T5020 WHERE F01 = ? LIMIT 1")) {
			pstmt.setInt(1, bankId);
			try (ResultSet resultSet = pstmt.executeQuery()) {
				if (resultSet.next()) {
					return resultSet.getString(1);
				}
			}
		}
		return "";
	}
    
    protected String selectT5019(Connection connection, int F01) throws SQLException {
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F14 FROM S50.T5019 WHERE T5019.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString(1);
                }
            }
        }
        return "";
    }
    
    public void freezeUserAccount(int orderId) throws Throwable {
    	try (Connection connection = this.getConnection()) {
            try
            {
                T6503 t6503 = this.selectT6503(orderId);
                
                serviceResource.openTransactions(connection);
                // 用户往来账户信息
                T6101 wlzh = selectT6101(t6503.F02, T6101_F03.WLZH, true);
                if (wlzh == null)
                {
                    throw new LogicalException("用户往来账户不存在");
                }
                
                // 用户锁定账户信息
                T6101 sdzh = selectT6101(t6503.F02, T6101_F03.SDZH, true);
                if (sdzh == null)
                {
                    throw new LogicalException("用户锁定账户不存在");
                }
                
                wlzh.F06 = wlzh.F06.subtract(t6503.F03).subtract(t6503.F04);
                if (wlzh.F06.compareTo(BigDecimal.ZERO) < 0)
                {
                    throw new LogicalException("用户账户可用金额不足");
                }
                this.updateT6101(connection, wlzh.F06, wlzh.F01);
                // 往来账户流水
                T6102 t6102 = new T6102();
                t6102.F02 = wlzh.F01;
                t6102.F03 = FeeCode.TX;
                t6102.F04 = sdzh.F01;
                t6102.F07 = t6503.F03;
                t6102.F08 = wlzh.F06.add(t6503.F04);
                t6102.F09 = "冻结提现金额";
                insertT6102(connection, t6102);
                if (t6503.F04.compareTo(BigDecimal.ZERO) > 0)
                {
                    // 往来账户流水
                    T6102 t6102s = new T6102();
                    t6102s.F02 = wlzh.F01;
                    t6102s.F03 = FeeCode.TX_SXF;
                    t6102s.F04 = sdzh.F01;
                    t6102s.F07 = t6503.F04;
                    t6102s.F08 = wlzh.F06;
                    t6102s.F09 = "冻结提现手续费";
                    insertT6102(connection, t6102s);
                }
                
                sdzh.F06 = sdzh.F06.add(t6503.F03).add(t6503.F04);
                this.updateT6101(connection, sdzh.F06, sdzh.F01);
                // 锁定账户流水
                T6102 sdt6102 = new T6102();
                sdt6102.F02 = sdzh.F01;
                sdt6102.F03 = FeeCode.TX;
                sdt6102.F04 = wlzh.F01;
                sdt6102.F06 = t6503.F03;
                sdt6102.F08 = sdzh.F06.subtract(t6503.F04);
                sdt6102.F09 = "冻结提现金额";
                insertT6102(connection, sdt6102);
                if (t6503.F04.compareTo(BigDecimal.ZERO) > 0)
                {
                    // 锁定账户流水
                    T6102 sdt6102s = new T6102();
                    sdt6102s.F02 = sdzh.F01;
                    sdt6102s.F03 = FeeCode.TX_SXF;
                    sdt6102s.F04 = wlzh.F01;
                    sdt6102s.F06 = t6503.F04;
                    sdt6102s.F08 = sdzh.F06;
                    sdt6102s.F09 = "冻结提现手续费";
                    insertT6102(connection, sdt6102s);
                }
                serviceResource.commit(connection);
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                logger.error("WithdrawServiceImpl.freezeUserAccount()".concat(e.getMessage()));
                throw e;
            }

    	}
    }
    
    private int insertT6130(Connection connection, int accountId, int cardId, BigDecimal funds, BigDecimal poundage, Timestamp timestamp, String bankCode, String cityCode) throws Throwable {
    	int id = 0;
        try (PreparedStatement ps =
            connection.prepareStatement("INSERT INTO S61.T6130(F02,F03,F04,F06,F07,F08,F09,F16,F17,F18,F20,F21) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)", PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, accountId);
            ps.setInt(2, cardId);
            ps.setBigDecimal(3, funds);
            ps.setBigDecimal(4, poundage);
            ps.setBigDecimal(5, poundage);
            ps.setTimestamp(6, timestamp);
            ps.setString(7, T6130_F09.DSH.toString());
            ps.setString(8, T6130_F16.F.name());
            Map<String, Object> result = getEmpInfo(accountId, connection);
            ps.setString(9, (String)result.get("empNum"));
            ps.setString(10, (String)result.get("empStatus"));
            ps.setString(11, cityCode);
            ps.setString(12, bankCode);
            ps.execute();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    id = rs.getInt(1);
                }
            }
        }
        return id;
    }
    
    protected Map<String, Object> getEmpInfo(int userId, Connection connection) throws SQLException {
        Map<String, Object> result = new HashMap<String, Object>();
        String sql =  "SELECT T7110.F12 AS F01, T7110.F05 AS F02 FROM S61.T6110 LEFT JOIN S71.T7110 ON T6110.F14 = T7110.F12 WHERE T6110.F01 = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()) {
                    result.put("empNum", rs.getString(1));
                    result.put("empStatus", rs.getString(2));
                }
            }
        }
        
        if (result.get("empNum") == null || StringHelper.isEmpty((String)result.get("empNum"))) {
            sql = "SELECT T7110.F12 AS F01, T7110.F05 AS F02 FROM S61.T6110 LEFT JOIN S71.T7110 ON T6110.F14 = T7110.F12 WHERE T6110.F01 IN (SELECT F01 FROM S61.T6111 T1 WHERE F02 = (SELECT T2.F03 FROM S61.T6111 T2 WHERE T2.F01 = ? LIMIT 1)) LIMIT 1 ";
            try (PreparedStatement ps = connection.prepareStatement(sql)) {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        result.put("empNum", rs.getString(1));
                        result.put("empStatus", rs.getString(2));
                    }
                }
            }
        }
        return result;
    }

	
	/**
     * 请求信息集合
     * @param requestEntity 请求实体类
     * @return 返回map集合
     */
    public List<NameValuePair> getRequestParams(int orderId) throws Throwable {
    	try (Connection connection = this.getConnection()) {
    		T6503 t6503 = this.selectT6503(connection, orderId);
    		T6141 t6141 = this.selectT6141(connection, t6503.F02);
    		T6130 t6130 = this.selectT6130(connection, t6503.F09);
	    	ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
	    	List<NameValuePair> params = new ArrayList<NameValuePair>();
	        
	        
	        // 商户代码 富友分配给各合作商户的唯一识别码	必填	
	        String merId = configureProvider.format(FuyouPayVaribles.DAIFU_MERID); 
	        String reqtype = "payforreq";                                     // 请求类型
	        String mchnt_key = configureProvider.format(FuyouPayVaribles.DAIFU_KEY);   // 商户密钥  
	        
	        Payforreq order = new Payforreq();
	        order.setVer("1.00");                                 // 版本号     否
	        order.setMerdt(FuYouMD5.formatDate("yyyyMMdd", null));// 请求日期 否        yyyyMMdd        
	        order.setOrderno(merId.concat(String.valueOf(orderId)));         // 请求流水 否	数字串，当天必须唯一                         
	        order.setBankno(t6130.F21);         // 总行代码 否	参见总行代码表                            
	        order.setCityno(t6130.F20);         // 城市代码 否	参见地市代码表  	                        
	        order.setAccntno(t6503.F06);      // 账号         否	用户账号      
	        
//	        try {
	            order.setBranchnm("");    // 支行名称 是	支行名称，中行、建行、广发必填        
				order.setAccntnm(t6141.F02);     // 账户名称 否	用户账户名称 
//			} catch (UnsupportedEncodingException e1) {
//				e1.printStackTrace();
//			}
			                                
	        order.setAmt(String.valueOf(t6503.F03.multiply(new BigDecimal(100)).setScale(0)));   // 金额         否	单位：分  
	        order.setEntseq("");                                  // 企业流水号，填写后，系统体现在交易查询和外部通知中           
	        order.setMemo("");                                    // 备注，填写后，系统体现在交易查询和外部通知中
	        order.setMobile("");                                  // 手机号，为将来短信通知时使用
	        
	        // 响应参数		否	使用XML表示，见各功能点的响应报文定义
	        try {
				String xml = Bean2XmlUtils.bean2xml(order, Payforreq.class);
		        String signDataStr = merId + "|" + mchnt_key + "|" + reqtype + "|" + xml;
		        logger.info("签名前参数字符串：" + signDataStr);
		        // 校验值		否	商户号|商户密码|请求类型|响应参数XML，用竖线拼接后用MD5散列后的16进制文本
		        String mac = FuYouMD5.MD5Encode(signDataStr);
		        logger.info("MD5签名后：" + mac);
		       	
		        
		        params.add(new BasicNameValuePair("merid", merId));
		        params.add(new BasicNameValuePair("reqtype", reqtype));
		        params.add(new BasicNameValuePair("xml", xml));
		        params.add(new BasicNameValuePair("mac", mac));		        
			} catch (JAXBException e) {
				e.printStackTrace();
			}    
	        return params;
    	}
    }
            
    private int psdInputCount(Connection connection) throws Throwable {
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        boolean isOpenWithPsd = BooleanParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
        if (!isOpenWithPsd) {
            // 如果不需要交易密码，则不校验
            return 0;
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F11 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, serviceResource.getSession().getAccountId());
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getShort(1);
                }
            }
        }
        return 0;
    }
    
    private boolean isYuqi(Connection connection, int accountId) throws Throwable {
        String sql = "SELECT DATEDIFF(?,F08) FROM S62.T6252 WHERE F09=? AND F03=? AND F08 < SYSDATE()";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setTimestamp(1, getCurrentTimestamp(connection));
            ps.setString(2, T6252_F09.WH.name());
            ps.setInt(3, accountId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int days = rs.getInt(1);
                    if (days > 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    protected Timestamp getCurrentTimestamp(Connection connection) throws Throwable {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()")) {
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getTimestamp(1);
                }
            }
        }
        return null;
    }
    
    protected int insertT6501(Connection connection, T6501 entity)  throws Throwable {
        int orderId = 0;      
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6501 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F10 = ?,F11 = ?,F12 = ?,F13 = ?", PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, entity.F02);
            pstmt.setString(2, entity.F03.name());
            pstmt.setTimestamp(3, entity.F04);
            pstmt.setTimestamp(4, entity.F05);
            pstmt.setTimestamp(5, entity.F06);
            pstmt.setString(6, entity.F07.name());
            pstmt.setInt(7, entity.F08);
            pstmt.setString(8, entity.F10);
            pstmt.setString(9, entity.F11 == null ? T6501_F11.F.name() : entity.F11.name());
            pstmt.setString(10, entity.F12);
            pstmt.setBigDecimal(11, entity.F13);            
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys()) {
                if (resultSet.next()) {
                    orderId = resultSet.getInt(1);
                }
            }
        }
        if (orderId == 0) {
            logger.error("AbstractP2PService.insertT6501():数据库异常");
            throw new SQLException("数据库异常");
        }
        return orderId;        
    }
    
    public T6110 selectT6110(int userId) throws Throwable {
        try (Connection connection = getConnection()){
            T6110 record = null;
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10, F15, F18, F19 FROM S61.T6110 WHERE T6110.F01 = ? LIMIT 1")){
                pstmt.setInt(1, userId);
                try (ResultSet resultSet = pstmt.executeQuery()) {
                    if (resultSet.next()) {
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
                        record.F15 = resultSet.getTimestamp(11);
                        record.F18 = T6110_F18.parse(resultSet.getString(12));
                        record.F19 = T6110_F19.parse(resultSet.getString(13));
                    }
                }
            }
            return record;
        }
    }
    
    public T6141 selectT6141(Connection connection, int userId) throws Throwable {
        T6141 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08 FROM S61.T6141 WHERE T6141.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, userId);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6141();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getString(2);
                    record.F03 = T6141_F03.parse(resultSet.getString(3));
                    record.F04 = T6141_F04.parse(resultSet.getString(4));
                    record.F05 = resultSet.getString(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = StringHelper.decode(resultSet.getString(7));
                    record.F08 = resultSet.getDate(8);
                }
            }
        }
        return record;
    }
    
    protected T6503 selectT6503(Connection connection, int F01) throws SQLException {
        T6503 record = null;
        try (PreparedStatement pstmt =
            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S65.T6503 WHERE T6503.F01 = ? LIMIT 1")) {
            pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6503();
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getBigDecimal(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F05 = resultSet.getBigDecimal(5);
                    record.F06 = resultSet.getString(6);
                    record.F07 = resultSet.getInt(7);
                    record.F08 = resultSet.getString(8);
                    record.F09 = resultSet.getInt(9);
                    record.F10 = resultSet.getBigDecimal(10);
                }
            }
        }
        return record;
    }
    
    public T6130 selectT6130(Connection connection, int F01) throws Throwable {
    	T6130 record = null;
    	try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F06, F07, F08, F09, F20, F21 FROM S61.T6130 WHERE F01=?")) {
    		pstmt.setInt(1, F01);
            try (ResultSet resultSet = pstmt.executeQuery()) {
                if (resultSet.next()) {
                    record = new T6130();                    
                    record.F01 = resultSet.getInt(1);
                    record.F02 = resultSet.getInt(2);
                    record.F03 = resultSet.getInt(3);
                    record.F04 = resultSet.getBigDecimal(4);
                    record.F06 = resultSet.getBigDecimal(5);
                    record.F07 = resultSet.getBigDecimal(6);
                    record.F08 = resultSet.getTimestamp(7);
                    record.F09 = T6130_F09.parse(resultSet.getString(8));
                    record.F20 = resultSet.getString(9);
                    record.F21 = resultSet.getString(10);
                }
            }
        }
    	return record;
    }
    
    @Override
	public void updateT6501(int orderId, T6501_F03 F03) throws Throwable {
    	try (Connection connection = this.getConnection()) {
    		T6503 t6503 = this.selectT6503(connection, orderId);
	        try (PreparedStatement pstmt = connection
	            .prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = SYSDATE() WHERE F01 = ?")) {
	            pstmt.setString(1, F03.name());
	            pstmt.setInt(2, orderId);
	            pstmt.execute();
	        }
	        if (F03 == T6501_F03.SB) {
		        try (PreparedStatement ps = connection.prepareStatement("UPDATE S61.T6130 SET F09 = ? ,F11 = now(),F14 =now(), F16= ? WHERE F01 = ?")){
	                ps.setString(1, T6130_F09.TXSB.name());
	                ps.setString(2, T6130_F16.F.name());
	                ps.setInt(3, t6503.F09);
	                ps.execute();
	            }
	        }
    	}
    }
    
    protected T6503 selectT6503(int F01) throws SQLException {
        T6503 record = null;
        try (Connection connection = this.getConnection()) {
	        try (PreparedStatement pstmt =
	            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S65.T6503 WHERE T6503.F01 = ? LIMIT 1")) {
	            pstmt.setInt(1, F01);
	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                if (resultSet.next()) {
	                    record = new T6503();
	                    record.F01 = resultSet.getInt(1);
	                    record.F02 = resultSet.getInt(2);
	                    record.F03 = resultSet.getBigDecimal(3);
	                    record.F04 = resultSet.getBigDecimal(4);
	                    record.F05 = resultSet.getBigDecimal(5);
	                    record.F06 = resultSet.getString(6);
	                    record.F07 = resultSet.getInt(7);
	                    record.F08 = resultSet.getString(8);
	                    record.F09 = resultSet.getInt(9);
	                    record.F10 = resultSet.getBigDecimal(10);
	                }
	            }
	        }
        }
        return record;
    }
    
    public T6501 selectT6501(int F01) throws Throwable {
    	T6501 record = null;
        try (Connection connection = this.getConnection()) {
	        try (PreparedStatement pstmt =
	            connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10 FROM S65.T6501 WHERE T6501.F01 = ? LIMIT 1")) {
	            pstmt.setInt(1, F01);
	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                if (resultSet.next()) {
	                    record = new T6501();
	                    record.F01 = resultSet.getInt(1);
	                    record.F02 = resultSet.getInt(2);
	                    record.F03 = T6501_F03.parse(resultSet.getString(3));
	                    record.F08 = resultSet.getInt(8);
	                    record.F09 = resultSet.getInt(9);
	                    record.F10 = resultSet.getString(10);
	                }
	            }
	        }
        }
        return record;
    }
    
    /**
     * 请求信息集合
     * @param requestEntity 请求实体类
     * @return 返回map集合
     */
    public Map<String, String> queryRequestParams(int orderId) throws Throwable {
    	try (Connection connection = this.getConnection()) {
	    	ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
	        Map<String, String> params = new LinkedHashMap<String, String>();
	        
	        // 商户代码 富友分配给各合作商户的唯一识别码	必填	
	        String merId = configureProvider.format(FuyouPayVaribles.DAIFU_MERID); 
	        String reqtype = "qrytransreq";                                     // 请求类型
	        String mchnt_key = configureProvider.format(FuyouPayVaribles.DAIFU_KEY);   // 商户密钥  
	        
	        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
			String dateString = formatter.format(new Date());
	        Qrytransreq req = new Qrytransreq();
	        
	        req.setVer("1.00");                                 // 版本号     否
	        req.setBusicd("AP01"); // 业务代码 代收（AC01）,代付（AP01）,退票（TP01）
	        req.setOrderno(merId.concat(String.valueOf(orderId)));         // 原请求流水 是代收、代付中接口的orderno
	        req.setStartdt(dateString); // 开始日期
	        req.setEnddt(dateString);   // 结束日期 否日期段不能超过15 天
	        req.setTransst(""); // 交易状态 是参见交易状态码
	        
	        // 响应参数		否	使用XML表示，见各功能点的响应报文定义
	        try {
				String xml = Bean2XmlUtils.bean2xml(req, Qrytransreq.class);
		        String signDataStr = merId + "|" + mchnt_key + "|" + reqtype + "|" + xml;
		        logger.info("签名前参数字符串：" + signDataStr);
		        // 校验值		否	商户号|商户密码|请求类型|响应参数XML，用竖线拼接后用MD5散列后的16进制文本
		        String mac = FuYouMD5.MD5Encode(signDataStr);
		        logger.info("MD5签名后：" + mac);
		       	
		        params.put("merid", merId);
		        params.put("reqtype", reqtype);
		        params.put("xml", xml);	                                                        
		        params.put("mac", mac); 
			} catch (JAXBException e) {
				e.printStackTrace();
			}    
	        return params;
    	}
    }
    
    public void trade(int orderId, String resson) throws Throwable {
    	T6503 t6503 = selectT6503(orderId);        
        try (Connection connection = this.getConnection()) {        	
            int pid = 0; // 平台用户id
            try (PreparedStatement ps = connection.prepareStatement("SELECT F01 FROM S71.T7101 LIMIT 1")) {
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        pid = resultSet.getInt(1);
                    }
                }
            }
            // 用户锁定账户信息
            T6101 uT6101 = selectT6101(t6503.F02, T6101_F03.WLZH, true);
            if (uT6101 == null) {
                throw new LogicalException("用户账户不存在");
            }
                      
            // 平台往来账户信息
            T6101 cT6101 = selectT6101(pid, T6101_F03.WLZH, true);
            if (cT6101 == null) {
                throw new LogicalException("平台往来账户不存在");
            }
            // 插资金流水
            {
                uT6101.F06 = uT6101.F06.add(t6503.F03);
                T6102 t6102 = new T6102();
                t6102.F02 = uT6101.F01;
                t6102.F03 = FeeCode.TX;
                t6102.F04 = uT6101.F01;
                t6102.F06 = t6503.F03;
                t6102.F08 = uT6101.F06;
                t6102.F09 = "用户提现失败返回";
                insertT6102(connection, t6102);                
            }
            if (t6503.F05.compareTo(BigDecimal.ZERO) > 0) {
                uT6101.F06 = uT6101.F06.add(t6503.F05);
                T6102 t6102 = new T6102();
                t6102.F02 = uT6101.F01;
                t6102.F03 = FeeCode.TX_SXF;
                t6102.F04 = uT6101.F01;
                t6102.F06 = t6503.F05;
                t6102.F08 = uT6101.F06;
                t6102.F09 = "用户提现手续费返回";
                insertT6102(connection, t6102);	 	                
            } else {
	            if (t6503.F04.compareTo(BigDecimal.ZERO) > 0) {
	                cT6101.F06 = cT6101.F06.subtract(t6503.F04);
	                updateT6101(connection, cT6101.F06, cT6101.F01);
	                {
	                    T6102 t6102 = new T6102();
	                    t6102.F02 = cT6101.F01;
	                    t6102.F03 = FeeCode.TX_SXF;
	                    t6102.F04 = uT6101.F01;
	                    t6102.F07 = t6503.F04;
	                    t6102.F08 = cT6101.F06;
	                    t6102.F09 = "用户提现手续费返回";
	                    insertT6102(connection, t6102);
	                }
	            }
            }
            updateT6101(connection, uT6101.F06, uT6101.F01);
            
            if (t6503.F09 > 0) {
            	// 修改提现申请放款状态
                try (PreparedStatement ps =
                    connection.prepareStatement("UPDATE S61.T6130 SET F09 = ? ,F11 = now(),F13 = ?,F14 =now(),F15 = ?, F16= ? WHERE F01 = ?")) {
                    ps.setString(1, T6130_F09.TXSB.name());
                    ps.setInt(2, 1000);
                    ps.setString(3, resson);
                    ps.setString(4, T6130_F16.F.name());
                    ps.setInt(5, t6503.F09);
                    ps.execute();
                }
            }  
            
            if (orderId > 0) {
            	try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = SYSDATE() WHERE F01 = ?")) {
    	            pstmt.setString(1, T6501_F03.SB.name());
    	            pstmt.setInt(2, orderId);
    	            pstmt.execute();
    	        }
            }
            
            ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
            Envionment envionment = configureProvider.createEnvionment();
            envionment.set("name", uT6101.F05);
            envionment.set("datetime", DateTimeParser.format(new Date()));
            BigDecimal je = t6503.F03;
            envionment.set("amount", je.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            String content = configureProvider.format(LetterVariable.TX_SB, envionment);
            sendLetter(connection, uT6101.F02, "提现失败", content);
            T6110 t6110 = selectT6110(uT6101.F02);
            String isUseYtx = configureProvider.getProperty(SmsVaribles.SMS_IS_USE_YTX);
            if ("1".equals(isUseYtx)) {
                SMSUtils smsUtils = new SMSUtils(configureProvider);
                int type = smsUtils.getTempleId(MsgVariavle.TX_SB.getDescription());
                sendMsg(connection,
                    t6110.F04, smsUtils.getSendContent(envionment.get("name"), envionment.get("datetime"), envionment.get("amount")), type);                
            } else  {
                String msgContent = configureProvider.format(MsgVariavle.TX_SB, envionment);
                sendMsg(connection, t6110.F04, msgContent);
            }
        }
    }
    
    private T6101 selectT6101(int F02, T6101_F03 F03, boolean bool) throws SQLException {
        T6101 record = null;
        StringBuffer sql = new StringBuffer("SELECT F01, F02, F03, F04, F05, F06, F07 FROM S61.T6101 WHERE T6101.F02 = ? AND T6101.F03 = ?");
        if (bool) {
        	sql.append(" FOR UPDATE");
        }
        try(Connection connection = this.getConnection()){
	        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
	            pstmt.setInt(1, F02);
	            pstmt.setString(2, F03.name());
	            try (ResultSet resultSet = pstmt.executeQuery()) {
	                if (resultSet.next()) {
	                    record = new T6101();
	                    record.F01 = resultSet.getInt(1);
	                    record.F02 = resultSet.getInt(2);
	                    record.F03 = T6101_F03.parse(resultSet.getString(3));
	                    record.F04 = resultSet.getString(4);
	                    record.F05 = resultSet.getString(5);
	                    record.F06 = resultSet.getBigDecimal(6);
	                    record.F07 = resultSet.getTimestamp(7);
	                }
	            }
	        }
        }
        return record;
    }
    
    private int insertT6102(Connection connection, T6102 entity) throws SQLException {
		try (PreparedStatement pstmt = connection
				.prepareStatement(
						"INSERT INTO S61.T6102 SET F02 = ?, F03 = ?, F04 = ?, F05 = CURRENT_TIMESTAMP(), F06 = ?, F07 = ?, F08 = ?, F09 = ?",
						PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, entity.F02);
			pstmt.setInt(2, entity.F03);
			pstmt.setInt(3, entity.F04);
			pstmt.setBigDecimal(4, entity.F06);
			pstmt.setBigDecimal(5, entity.F07);
			pstmt.setBigDecimal(6, entity.F08);
			pstmt.setString(7, entity.F09);
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	private void updateT6101(Connection connection, BigDecimal F01, int F02) throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S61.T6101 SET F06 = ? WHERE F01 = ?")) {
			pstmt.setBigDecimal(1, F01);
			pstmt.setInt(2, F02);
			pstmt.execute();
		}
	}
		
	protected void sendLetter(Connection connection, int userId, String title, String content) throws Throwable {
		int letterId = insertT6123(connection, userId, title, T6123_F05.WD);
		insertT6124(connection, letterId, content);
	}
	
	private int insertT6123(Connection connection, int F02, String F03,
			T6123_F05 F05) throws Throwable {
		try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6123 SET F02 = ?, F03 = ?, F04 = ?, F05 = ?",PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setInt(1, F02);
			pstmt.setString(2, F03);
			pstmt.setTimestamp(3, getCurrentTimestamp(connection));
			pstmt.setString(4, F05.name());
			pstmt.execute();
			try (ResultSet resultSet = pstmt.getGeneratedKeys();) {
				if (resultSet.next()) {
					return resultSet.getInt(1);
				}
				return 0;
			}
		}
	}

	protected void insertT6124(Connection connection, int F01, String F02)	throws SQLException {
		try (PreparedStatement pstmt = connection.prepareStatement("INSERT INTO S61.T6124 SET F01 = ?, F02 = ?")) {
			pstmt.setInt(1, F01);
			pstmt.setString(2, F02);
			pstmt.execute();
		}
	}
	
	/**
     * 容联云通讯发送短信方法
     * @param connection
     * @param mobile 手机
     * @param content 内容
     * @param tempId 模板id
     * @throws Throwable
     */
    protected void sendMsg(Connection connection, String mobile, String content, int tempId) throws Throwable {
        try {
            if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile)){
                long msgId = 0;
                try (PreparedStatement ps = connection.prepareStatement("INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)",  Statement.RETURN_GENERATED_KEYS)) {
                    ps.setInt(1, tempId);
                    ps.setString(2, content);
                    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    ps.setString(4, "W");
                    ps.execute();
                    try (ResultSet resultSet = ps.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            msgId = resultSet.getLong(1);
                        }
                    }
                }
                if (msgId > 0) {
                    try (PreparedStatement ps =
                        connection.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)")) {
                        ps.setLong(1, msgId);
                        ps.setString(2, mobile);
                        ps.execute();
                    }
                }
                return;
            }
        } catch (Exception e) {
            logger.error(e, e);
            throw e;
        }
    }
	
	protected void sendMsg(Connection connection, String mobile, String content) throws Throwable {
        try {
            if (!StringHelper.isEmpty(content) && !StringHelper.isEmpty(mobile)) {
                long msgId = 0;
                try (PreparedStatement ps = connection.prepareStatement("INSERT INTO S10._1040(F02,F03,F04,F05) values(?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
                	ps.setInt(1, 0);
                    ps.setString(2, content);
                    ps.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
                    ps.setString(4, "W");
                    ps.execute();
                    try (ResultSet resultSet = ps.getGeneratedKeys()) {
                        if (resultSet.next()) {
                            msgId = resultSet.getLong(1);
                        }
                    }
                }
                if (msgId > 0) {
                    try (PreparedStatement ps = connection.prepareStatement("INSERT INTO S10._1041(F01,F02) VALUES(?,?)")) {
                        ps.setLong(1, msgId);
                        ps.setString(2, mobile);
                        ps.execute();
                    }
                }
                return;
            }
        } catch (Exception e){
            logger.error(e, e);
            throw e;
        }
    }
		
}
