package com.dimeng.p2p.app.achieve;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.data.sql.SQLConnectionProvider;
import com.dimeng.framework.resource.ResourceNotFoundException;
import com.dimeng.framework.service.AbstractService;
import com.dimeng.framework.service.ServiceResource;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.P2PConst;
import com.dimeng.p2p.PaymentInstitution;
import com.dimeng.p2p.S61.entities.T6119_EXT;
import com.dimeng.p2p.S61.enums.T6114_F14;
import com.dimeng.p2p.S61.enums.T6118_F02;
import com.dimeng.p2p.S61.enums.T6118_F03;
import com.dimeng.p2p.S61.enums.T6118_F05;
import com.dimeng.p2p.S61.enums.T6119_EXT_F10;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.entities.T6502;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.app.entity.BindQueryResult;
import com.dimeng.p2p.app.entity.CheckResult;
import com.dimeng.p2p.app.entity.NewProtocolBindXmlBeanReq;
import com.dimeng.p2p.app.entity.NewProtocolOrderXmlBeanReq;
import com.dimeng.p2p.app.entity.NewProtocolQueryXmlBeanReq;
import com.dimeng.p2p.app.entity.PayResult;
import com.dimeng.p2p.app.entity.SignMsgResult;
import com.dimeng.p2p.app.entity.SignResult;
import com.dimeng.p2p.app.entity.TerminationResult;
import com.dimeng.p2p.app.service.FuYouPayManageService;
import com.dimeng.p2p.app.utils.HttpPoster;
import com.dimeng.p2p.app.utils.XMapUtil;
import com.dimeng.p2p.modules.account.pay.service.entity.Auth;
import com.dimeng.p2p.modules.account.pay.service.entity.ChargeOrder;
import com.dimeng.p2p.pay.service.fuyou.util.Bean2XmlUtils;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BigDecimalParser;
import com.fuiou.mpay.encrypt.DESCoderFUIOU;
import com.fuiou.mpay.encrypt.RSAUtils;
import com.fuiou.util.MD5;

public class FuYouPayManageServiceImpl extends AbstractService implements FuYouPayManageService
{
	ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
    public FuYouPayManageServiceImpl(ServiceResource serviceResource)
    {
        super(serviceResource);
    }
    
    protected Connection getConnection()
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection();
    }
    
  
  
    /**
     * <一句话功能简述>
     * <功能详细描述> 查詢协议支付签约信息
     * @param accountId
     * @return
     * @throws Throwable
     */
    private T6119_EXT selectT6119_EXT(int accountId)
        throws Throwable
    {
        T6119_EXT t6119_ext = new T6119_EXT();
        try (Connection connection = this.getConnection())
        {
            try (PreparedStatement pstmt =
                connection.prepareStatement("SELECT F01, F02, F03, F04, F05, F06, F07, F08, F09, F10,F11,F12 FROM S61.T6119_EXT WHERE F02 = ? ORDER BY F01 DESC LIMIT 1 "))
            {
                pstmt.setInt(1, accountId);
                try (ResultSet resultSet = pstmt.executeQuery())
                {
                    if (resultSet.next())
                    {
                        t6119_ext.F01 = resultSet.getInt(1);
                        t6119_ext.F02 = resultSet.getInt(2);
                        t6119_ext.F03 = resultSet.getString(3);
                        t6119_ext.F04 = resultSet.getString(4);
                        t6119_ext.F05 = resultSet.getString(5);
                        t6119_ext.F06 = resultSet.getString(6);
                        t6119_ext.F07 = resultSet.getString(7);
                        t6119_ext.F08 = resultSet.getString(8);
                        t6119_ext.F09 = resultSet.getString(9);
                        t6119_ext.F10 = T6119_EXT_F10.parse(resultSet.getString(10));
                        t6119_ext.F11 = resultSet.getTimestamp(11);
                    }
                }
            }
        }
        return t6119_ext;
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述> 通联协议支付签约成功更新协议号
     * @param connection
     * @param F01
     * @param F02
     * @throws SQLException
     */
    private void updateSign(Connection connection, String F01, int F02, T6119_EXT ext)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S61.T6119_EXT SET F04 = ?,F10=? WHERE F02 = ? AND F01=? "))
        {
            pstmt.setString(1, F01);
            pstmt.setString(2, T6119_EXT_F10.S.name());
            pstmt.setInt(3, F02);
            pstmt.setInt(4, ext.F01);
            pstmt.execute();
        }
        
        try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S61.T6110 SET F04 = ? WHERE F01=? "))
            {
                pstmt.setString(1, ext.F09);
                pstmt.setInt(2, F02);
                pstmt.execute();
            }
        
        // 修改用户 原来旧的未成功的请求    
        try (PreparedStatement pstmt =
        // connection.prepareStatement("UPDATE S61.T6119_EXT SET F10=? WHERE F02 = ? AND F10=? AND  DATE_ADD(F11,INTERVAL 10 DAY_MINUTE)"))
            connection.prepareStatement("UPDATE S61.T6119_EXT SET F10=? WHERE F02 = ? AND F10=? AND  F11 < date_sub(CURRENT_TIMESTAMP(),INTERVAL 10 DAY_MINUTE)"))
        {
            pstmt.setString(1, T6119_EXT_F10.YSX.name());
            pstmt.setInt(2, F02);
            pstmt.setString(3, T6119_EXT_F10.F.name());
            pstmt.execute();
        }
    }
    

    

    
    /**
     * 更新订单表
     * 
     * @param connection
     * @param F01
     * @param F02
     * @param F03
     * @throws SQLException
     */
    protected void updateT6501(Connection connection, T6501_F03 F01, Timestamp F02, int F03)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE S65.T6501 SET F03 = ?, F06 = ? WHERE F01 = ?"))
        {
            pstmt.setString(1, F01.name());
            pstmt.setTimestamp(2, F02);
            pstmt.setInt(3, F03);
            pstmt.execute();
        }
    }
    
    /**
     * 删除协议支付签约
     * 
     * @param connection
     * @param F02
     * @throws SQLException
     */
    protected void deleteT6119_EXT(Connection connection, T6119_EXT t6119_ext, int F02)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE  S61.T6119_EXT SET F10=? WHERE F02 = ? AND F01=? "))
        {
            pstmt.setString(1, T6119_EXT_F10.YJY.name());
            pstmt.setInt(2, F02);
            pstmt.setInt(3, t6119_ext.F01);
            pstmt.execute();
        }
        
        try (PreparedStatement pstmt =
            connection.prepareStatement("UPDATE  S61.T6119_EXT SET F10=? WHERE F02 = ? AND F10 IN(?) "))
        {
            pstmt.setString(1, T6119_EXT_F10.YSX.name());
            pstmt.setInt(2, F02);
            pstmt.setString(3, T6119_EXT_F10.F.name());
            pstmt.execute();
        }
    }

	@Override
	public SignMsgResult signMsg(NewProtocolBindXmlBeanReq req,int id)
			throws Throwable {
		Map<String,String> map = new HashMap<String, String>();
		//String url = "http://www-1.fuiou.com:18670/mobile_pay/newpropay/bindMsg.pay";
		//请求地址
		String url = configureProvider.getProperty(SystemVariable.BINDMSGURL);
		//商户号
		String mchntcd =configureProvider.getProperty(SystemVariable.MCHNTCD);
		//商户密钥
		String key =configureProvider.getProperty(SystemVariable.KEY);
		//私钥
		String privatekey =configureProvider.getProperty(SystemVariable.PRIVATEKEY);
		req.setVersion("1.0");
		  //请求时间
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式 
        String tradeDate =  df.format(new Date()); 
        req.setTradeDate(tradeDate);
        req.setMchntCd(mchntcd);
        req.setIdType("0");
        req.setCvn("");
		req.setSign(getSign(req.sendMsgSignStr(key), "MD5", privatekey));	
		String APIFMS =XMapUtil.toXML(req, "UTF-8");;
		APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(key));
		map.put("MCHNTCD",mchntcd);
		map.put("APIFMS", APIFMS);
		String result = new HttpPoster(url).postStr(map);
		result = DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(key));
		logger.info("签约请求返回信息：" + result);
		SignMsgResult signMsgResult = Bean2XmlUtils.xml2bean(result, SignMsgResult.class); 
		if("0000".equals(signMsgResult.getRESPONSECODE())){
			//保存签约短信请求流水号
		    try (Connection connection = getConnection()){
		    	try {
		    		   insert(connection,
			                    "INSERT INTO S61.T6119_EXT SET F02 = ?, F03 = ?, F05 = ?, F06 = ?, F07 = ?, F08 = ?, F09 = ?,F11=? ",
			                    id,
			                    signMsgResult.getMCHNTSSN(),
			                    req.getCardNo(),
			                    req.getAccount(),
			                    req.getIdType(),
			                    req.getIdCard(),
			                    req.getMobileNo(),
			                    getCurrentTimestamp(connection));
				} catch (Exception e) {
					logger.error("数据库异常");
                    throw new SQLException("数据库异常");
				}
		     
		    }
	    
		}	
		return signMsgResult;
	}
     
    protected Timestamp getCurrentTimestamp(Connection connection)
        throws Throwable
    {
        try (PreparedStatement pstmt = connection.prepareStatement("SELECT CURRENT_TIMESTAMP()"))
        {
            try (ResultSet resultSet = pstmt.executeQuery())
            {
                if (resultSet.next())
                {
                    return resultSet.getTimestamp(1);
                }
            }
        }
        return null;
    }

	@Override
	public SignResult sign(NewProtocolBindXmlBeanReq req,int id ) throws Throwable {
		String url =configureProvider.getProperty(SystemVariable.BINDCOMMITURL);
		String mchntcd =configureProvider.getProperty(SystemVariable.MCHNTCD);
		//商户密钥
		String key =configureProvider.getProperty(SystemVariable.KEY);
		//查询签约时的流水号
		T6119_EXT t6119 = selectT6119_EXT(id);
	    //请求时间
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");//设置日期格式 
        String tradeDate =  df.format(new Date());
		req.setMchntSsn(t6119.F03);
		req.setAccount(t6119.F06);
		req.setCardNo(t6119.F05);
		req.setIdType(t6119.F07);
		req.setIdCard(t6119.F08);
		req.setMobileNo(t6119.F09);
		req.setVersion("1.0");
		req.setTradeDate(tradeDate);
		req.setMchntCd(mchntcd);
		req.setSign(getSign(req.proBindSignStr(key), "MD5",configureProvider.getProperty(SystemVariable.PRIVATEKEY)));
		Map<String,String> map = new HashMap<String, String>();
		String APIFMS =XMapUtil.toXML(req, "UTF-8");;
		APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(configureProvider.getProperty(SystemVariable.KEY)));
		map.put("MCHNTCD",mchntcd);
		map.put("APIFMS", APIFMS);
		String result = new HttpPoster(url).postStr(map);
		result = DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(configureProvider.getProperty(SystemVariable.KEY)));
		logger.info("签约请求返回信息：" + result);
		SignResult signResult = Bean2XmlUtils.xml2bean(result, SignResult.class);
		if("0000".equals(signResult.getRESPONSECODE())){
			//保存签约成功信息
            try (Connection connection = getConnection())
            {
                try
                {
                    // 协议签约请求记录表
                    updateSign(connection, signResult.getPROTOCOLNO(), id, t6119);
                    //用户第三方开户信息表
                    execute(connection,
                        "INSERT INTO S61.T6119 SET F01=?, F02=?,F03=?,F04=? ",
                        id,
                        PaymentInstitution.FUYOUPAY.getInstitutionCode(),
                        signResult.getPROTOCOLNO(),
                        "协议支付签约成功");
                    // 银行卡表
                    execute(connection,
                        "UPDATE  S61.T6114 SET F14=?,F16=? WHERE F02=? AND F07=? ",
                        T6114_F14.RZCG.name(),
                        "RZCG",
                        id,
                        t6119.F05);
                    // 用户认证表
                    execute(connection,
                        "UPDATE  S61.T6118 SET F02=?,F03=? WHERE F01=? ",
                        T6118_F02.TG.name(),
                        T6118_F03.TG.name(),
                        id);
                }
                catch (Exception e)
                {
                    logger.error("FuYouPayManageServiceImpl.updateSign():数据库异常", e);
                    throw new SQLException("数据库异常");
                }
            }
			
		}
		
		return signResult ;
	}

	@Override
	public TerminationResult termination(NewProtocolBindXmlBeanReq req,int id) throws Throwable {
		
		T6119_EXT t6119_ext = selectT6119_EXT(id);
		req.setProtocolNo(t6119_ext.F04);
		req.setSign(getSign(req.proUnBindSignStr(configureProvider.getProperty(SystemVariable.KEY)), "MD5",configureProvider.getProperty(SystemVariable.PRIVATEKEY)));	
		String url =configureProvider.getProperty(SystemVariable.UNBIND);
		String mchntcd =configureProvider.getProperty(SystemVariable.MCHNTCD);
		Map<String,String> map = new HashMap<String, String>();
		String APIFMS =XMapUtil.toXML(req, "UTF-8");;
		APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(configureProvider.getProperty(SystemVariable.KEY)));
		map.put("MCHNTCD",mchntcd);
		map.put("APIFMS", APIFMS);
		String result = new HttpPoster(url).postStr(map);
		result = DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(configureProvider.getProperty(SystemVariable.KEY)));
		logger.info("签约请求返回信息：" + result);
		TerminationResult terminationResult = Bean2XmlUtils.xml2bean(result, TerminationResult.class);
		if("0000".equals(terminationResult.getRESPONSECODE())){
			   
            try (Connection connection = getConnection())
            {
                try
                {
                    deleteT6119_EXT(connection, t6119_ext, id);
                    execute(connection, "DELETE FROM  S61.T6119 WHERE F01= ? ", id);
                    // 银行卡表
                    execute(connection,
                        "UPDATE  S61.T6114 SET F14=?,F16=? WHERE F02=? AND F07=? ",
                        T6114_F14.WRZ.name(),
                        "DRZ",
                        id,
                        t6119_ext.F05);
                    // 用户认证表
                    execute(connection,
                        "UPDATE  S61.T6118 SET F02=?,F03=? WHERE F01=? ",
                        T6118_F02.BTG.name(),
                        T6118_F03.TG.name(),
                        id);
                }
                catch (Exception e)
                {
                    logger.error("FuYouPayManageServiceImpl.deleteT6199():数据库异常", e);
                    throw new SQLException("数据库异常");
                }
            }
			
		}
		
		return terminationResult;
	}

	@Override
	public BindQueryResult bindQuery(NewProtocolBindXmlBeanReq req)
			throws Throwable {
		//协议卡查询地址
		String url = configureProvider.getProperty(SystemVariable.BINDQUERY);
		String mchntcd =configureProvider.getProperty(SystemVariable.MCHNTCD);
		//商户密钥
		String key =configureProvider.getProperty(SystemVariable.KEY);
		//私钥
		String  privatekey =configureProvider.getProperty(SystemVariable.PRIVATEKEY);
		req.setVersion("1.0");
		req.setMchntCd(mchntcd);
		req.setSign(getSign(req.querySignStr(key), "MD5", privatekey));
		Map<String,String> map = new HashMap<String, String>();
		String APIFMS =XMapUtil.toXML(req, "UTF-8");;
		APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(configureProvider.getProperty(SystemVariable.KEY)));
		map.put("MCHNTCD",mchntcd);
		map.put("APIFMS", APIFMS);
		String result = new HttpPoster(url).postStr(map);
		result = DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(configureProvider.getProperty(SystemVariable.KEY)));
		logger.info("签约请求返回信息：" + result);
		//返回信息解析
		BindQueryResult cardresult = Bean2XmlUtils.xml2bean(result, BindQueryResult.class);
		if ("0000".equals(cardresult.getRESPONSECODE())) {	
			logger.info("查询成功绑定协议卡号为：" + cardresult.getCARDNO());
		}
		return cardresult;
	}

	@Override
	public PayResult signPay(NewProtocolOrderXmlBeanReq req,int id) throws Throwable {
	
		Map<String,String> map = new HashMap<String, String>();
		//支付请求地址
		String url = configureProvider.getProperty(SystemVariable.ORDERPAY);
		//商户号
		String mchntcd =configureProvider.getProperty(SystemVariable.MCHNTCD);
		//商户密钥
		String key =configureProvider.getProperty(SystemVariable.KEY);
		//商户订单号
		if(req.getMchntOrderId()==null){
			String mchntorderid ="YLD"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + id; 
			req.setMchntOrderId(mchntorderid);
		}
		//后台通知 URL
		String backurl =configureProvider.getProperty(SystemVariable.BACKURL);
		//私钥
		String  privatekey =configureProvider.getProperty(SystemVariable.PRIVATEKEY);
		T6119_EXT t6119_ext = selectT6119_EXT(id);
		req.setVersion("1.0");
		req.setType("03");
		req.setMchntCd(mchntcd);
		
		if(!"".equals(t6119_ext.F04)&& t6119_ext!=null){
			req.setProtocolNo(t6119_ext.F04);
		}
/*		req.setUserId(userid);
		req.setAmt(amt);
		req.setUserIp(userip);
		*/
		req.setBackUrl(backurl);
		req.setNeedSendMsg("0");
		req.setSignTp("MD5");
		//签名
		req.setSign(getSign(req.signStr(key), "MD5", privatekey));
		String APIFMS =XMapUtil.toXML(req, "UTF-8");
		APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(key));
		map.put("MCHNTCD",mchntcd);
		map.put("APIFMS", APIFMS);
		String result = new HttpPoster(url).postStr(map);
		logger.info("签约请求返回信息：" + result);
		result = DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(key));
		PayResult payresult= Bean2XmlUtils.xml2bean(result, PayResult.class);
		return payresult;
	}
     
	/**
	 * 获取签名
	 * @param signStr  签名串
	 * @param signtp   签名类型
	 * @param key      密钥
	 * @return
	 * @throws Exception
	 */
	public static String getSign(String signStr,String signtp,String key) throws  Exception{
		String sign = "";
		if ("md5".equalsIgnoreCase(signtp)) {
			sign = MD5.MD5Encode(signStr);
		} else {
			sign =	RSAUtils.sign(signStr.getBytes("utf-8"), key);
		}
		return sign;
	}
	
    @Override
    public ChargeOrder addOrder(BigDecimal amount, int payCompanyCode)
        throws Throwable
    {
        if (serviceResource.getSession() == null || !serviceResource.getSession().isAuthenticated())
        {
            throw new ParameterException("鉴权失败!");
        }
        ConfigureProvider configureProvider = serviceResource.getResource(ConfigureProvider.class);
        String mPhone = configureProvider.getProperty(PayVariavle.CHARGE_MUST_PHONE);
        String mRealName = configureProvider.getProperty(PayVariavle.CHARGE_MUST_NCIIC);
        String mWithPsd = configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD);
        Auth auth = getAutnInfo();
        if ("true".equals(mPhone) && !auth.phone)
        {
            throw new LogicalException("手机号未认证!");
        }
        if ("true".equals(mRealName) && !auth.realName)
        {
            throw new LogicalException("未实名认证!");
        }
        if ("true".equals(mWithPsd) && !auth.withdrawPsw)
        {
            throw new LogicalException("交易密码未设置!");
        }
        if (amount.compareTo(new BigDecimal(0)) <= 0 || payCompanyCode <= 0)
        {
            throw new ParameterException("金额或支付类型错误!");
        }
        
        String min = configureProvider.getProperty(PayVariavle.CHARGE_MIN_AMOUNT);
        String max = configureProvider.getProperty(PayVariavle.CHARGE_MAX_AMOUNT);
        if (amount.compareTo(new BigDecimal(min)) < 0 || amount.compareTo(new BigDecimal(max)) > 0)
        {
            StringBuilder builder = new StringBuilder("充值金额必须大于");
            builder.append(min);
            builder.append("元小于");
            builder.append(max);
            builder.append("元!");
            throw new LogicalException(builder.toString());
        }
        String rate = configureProvider.getProperty(PayVariavle.CHARGE_RATE);
        if (StringHelper.isEmpty(rate))
        {
            return null;
        }
        BigDecimal ysPondage = amount.multiply(new BigDecimal(rate)); // 应收手续费
        BigDecimal maxPondage = BigDecimalParser.parse(configureProvider.getProperty(PayVariavle.CHARGE_MAX_POUNDAGE)); // 最大手续费限额
        BigDecimal ssPoundage = ysPondage.compareTo(maxPondage) >= 0 ? maxPondage : ysPondage; // 实收手续费
        
        try (Connection connection = getConnection())
        {
            try
            {
                serviceResource.openTransactions(connection);
                int accountId = serviceResource.getSession().getAccountId();
                T6501 t6501 = new T6501();
                t6501.F02 = OrderType.CHARGE.orderType();
                t6501.F03 = T6501_F03.DTJ;
                t6501.F07 = T6501_F07.YH;
                t6501.F08 = accountId;
                t6501.F13 = amount;  
                t6501.F04 = getCurrentTimestamp(connection);
                int oId = insertT6501(connection, t6501);
                if (oId <= 0)
                {
                    throw new LogicalException("数据库异常!");
                }
                T6502 t6502 = new T6502();
                t6502.F01 = oId;
                t6502.F02 = accountId;
                t6502.F03 = amount;
                t6502.F04 = ysPondage;
                t6502.F05 = ssPoundage;
                t6502.F07 = payCompanyCode;      
                insertT6502(connection, t6502);
                ChargeOrder order = new ChargeOrder();
                order.id = oId;
                order.amount = amount;
                order.orderTime = t6501.F04;
                order.payCompanyCode = payCompanyCode;
                serviceResource.commit(connection);
                return order;
            }
            catch (Exception e)
            {
                serviceResource.rollback(connection);
                throw e;
            }
            
        }
        
    }
    
   
    protected Connection getConnection(String db)
        throws ResourceNotFoundException, SQLException
    {
        return serviceResource.getDataConnectionProvider(SQLConnectionProvider.class, P2PConst.DB_MASTER_PROVIDER)
            .getConnection(db);
    }
    
    private Auth getAutnInfo()
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
                        auth.withdrawPsw = T6118_F05.YSZ.name().equals(resultSet.getString(3));
                    }
                }
            }
            return auth;
        }
    }
    
    protected int insertT6501(Connection connection, T6501 entity)
        throws Throwable
    {
        
        int orderId = 0;
        StringBuilder sql =
            new StringBuilder(
                "INSERT INTO S65.T6501 SET F02 = ?,F03 = ?,F04 = ?,F05 = ?,F06 = ?,F07 = ?,F08 = ?,F10 = ?,F11 = ?,F12 = ?,F13 = ?");
        if (entity.F09 != null)
        {
            sql.append(",F09 = ?");
        }
        try (PreparedStatement pstmt =
            connection.prepareStatement(sql.toString(), PreparedStatement.RETURN_GENERATED_KEYS))
        {
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
            if (entity.F09 != null)
            {
                pstmt.setInt(12, entity.F09);
            }
            pstmt.execute();
            try (ResultSet resultSet = pstmt.getGeneratedKeys();)
            {
                if (resultSet.next())
                {
                    orderId = resultSet.getInt(1);
                }
            }
        }
        if (orderId == 0)
        {
            logger.error("AbstractP2PService.insertT6501():数据库异常");
            throw new SQLException("数据库异常");
        }
        return orderId;
        
    }
    
    protected void insertT6502(Connection connection, T6502 entity)
        throws SQLException
    {
        try (PreparedStatement pstmt =
            connection.prepareStatement("INSERT INTO S65.T6502 SET F01 = ?, F02 = ?, F03 = ?, F04 = ?, F05 = ?, F06 = ?, F07 = ?, F09 = ?, F10 = ? "))
        {
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
            pstmt.execute();
        }
        
    }
    
    protected Map<String, Object> getEmpInfo(int userId, Connection connection)
        throws SQLException
    {
        Map<String, Object> result = new HashMap<String, Object>();
        String sql =
            "SELECT T7110.F12 AS F01, T7110.F05 AS F02 FROM S61.T6110 LEFT JOIN S71.T7110 ON T6110.F14 = T7110.F12 WHERE T6110.F01 = ? LIMIT 1";
        try (PreparedStatement ps = connection.prepareStatement(sql))
        {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery())
            {
                if (rs.next())
                {
                    result.put("empNum", rs.getString(1));
                    result.put("empStatus", rs.getString(2));
                }
            }
        }
        
        if (result.get("empNum") == null || StringHelper.isEmpty((String)result.get("empNum")))
        {
            sql =
                "SELECT T7110.F12 AS F01, T7110.F05 AS F02 FROM S61.T6110 LEFT JOIN S71.T7110 ON T6110.F14 = T7110.F12 WHERE T6110.F01 IN (SELECT F01 FROM S61.T6111 T1 WHERE F02 = (SELECT T2.F03 FROM S61.T6111 T2 WHERE T2.F01 = ? LIMIT 1)) LIMIT 1 ";
            try (PreparedStatement ps = connection.prepareStatement(sql))
            {
                ps.setInt(1, userId);
                try (ResultSet rs = ps.executeQuery())
                {
                    if (rs.next())
                    {
                        result.put("empNum", rs.getString(1));
                        result.put("empStatus", rs.getString(2));
                    }
                }
            }
        }
        return result;
    }
    
    @Override
    public void updateT6501(int orderId, String querySn)
        throws Throwable
    {
        
        try (Connection connection = getConnection())
        {
            try (PreparedStatement pstmt = connection.prepareStatement("UPDATE S65.T6501 SET F10 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, querySn);
                pstmt.setInt(2, orderId);
                pstmt.execute();
            }
        }
    }

	@Override
	public CheckResult chargeQuery(NewProtocolQueryXmlBeanReq req)
			throws Throwable {
		//http://www-1.fuiou.com:18670/mobile_pay/checkInfo/checkResult.pay
		String url = configureProvider.getProperty(SystemVariable.CHECKRESULT);
		String mchntcd =configureProvider.getProperty(SystemVariable.MCHNTCD);
		//商户密钥
		String key =configureProvider.getProperty(SystemVariable.KEY);
		//私钥
		String  privatekey =configureProvider.getProperty(SystemVariable.PRIVATEKEY);
		req.setVersion("3.0");
		req.setMchntCd(mchntcd);
	/*	req.setMchntOrderId(mchntOrderId);*/
		//签名
		req.setSign(getSign(req.signStr(key), "MD5", privatekey));
		String APIFMS =XMapUtil.toXML(req, "UTF-8");
		APIFMS = DESCoderFUIOU.desEncrypt(APIFMS, DESCoderFUIOU.getKeyLength8(key));
		Map<String,String> map = new HashMap<String, String>();
		map.put("MCHNTCD",mchntcd);
		map.put("APIFMS", APIFMS);
		String result = new HttpPoster(url).postStr(map);
		result = DESCoderFUIOU.desDecrypt(result,DESCoderFUIOU.getKeyLength8(key));
		logger.info("签约请求返回信息：" + result);
		CheckResult checkresult = Bean2XmlUtils.xml2bean(result, CheckResult.class); 
		return checkresult;
	}

	@Override
	public void updateT6502(int orderId, String F01, String F02) throws Throwable {
		 try (Connection connection = getConnection()){
        try (PreparedStatement pstmt =
                connection.prepareStatement("UPDATE S65.T6502 SET F08 = ?, F11 = ? WHERE F01 = ?"))
            {
                pstmt.setString(1, F01);
                pstmt.setString(2, F02);
                pstmt.setInt(3, orderId);
                pstmt.execute();
            }
        }
	}
}
