package com.dimeng.p2p.app.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 协议支付绑定响应信息实体类
 * @author raoyujun
 *
 */
@XmlRootElement(name = "RESPONSE")
public class PayResult {
	
	private String VERSION;  // 版本号
	private String TYPE; // 类型
	private String RESPONSECODE;//响应代码
	private String RESPONSEMSG;//响应中文描述
	private String MCHNTCD;//商户号
	private String USERID;//用户编号
	private String MCHNTORDERID; //商户订单号
	private String ORDERID; //富友订单号
	private String PROTOCOLNO; //协议号
	private String BANKCARD; //银行卡号
	private String AMT; //交易金额
	private String REM1; //保留字段 1
	private String REM2; //保留字段 2
	private String REM3; //保留字段 3
	private String SIGNTP; //签名类型
	private String SIGN; //摘要数据
	public String getVERSION() {
		return VERSION;
	}
	public void setVERSION(String vERSION) {
		VERSION = vERSION;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getRESPONSECODE() {
		return RESPONSECODE;
	}
	public void setRESPONSECODE(String rESPONSECODE) {
		RESPONSECODE = rESPONSECODE;
	}
	public String getRESPONSEMSG() {
		return RESPONSEMSG;
	}
	public void setRESPONSEMSG(String rESPONSEMSG) {
		RESPONSEMSG = rESPONSEMSG;
	}
	public String getMCHNTCD() {
		return MCHNTCD;
	}
	public void setMCHNTCD(String mCHNTCD) {
		MCHNTCD = mCHNTCD;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getMCHNTORDERID() {
		return MCHNTORDERID;
	}
	public void setMCHNTORDERID(String mCHNTORDERID) {
		MCHNTORDERID = mCHNTORDERID;
	}
	public String getORDERID() {
		return ORDERID;
	}
	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}
	public String getPROTOCOLNO() {
		return PROTOCOLNO;
	}
	public void setPROTOCOLNO(String pROTOCOLNO) {
		PROTOCOLNO = pROTOCOLNO;
	}
	public String getBANKCARD() {
		return BANKCARD;
	}
	public void setBANKCARD(String bANKCARD) {
		BANKCARD = bANKCARD;
	}
	public String getAMT() {
		return AMT;
	}
	public void setAMT(String aMT) {
		AMT = aMT;
	}
	public String getREM1() {
		return REM1;
	}
	public void setREM1(String rEM1) {
		REM1 = rEM1;
	}
	public String getREM2() {
		return REM2;
	}
	public void setREM2(String rEM2) {
		REM2 = rEM2;
	}
	public String getREM3() {
		return REM3;
	}
	public void setREM3(String rEM3) {
		REM3 = rEM3;
	}
	public String getSIGNTP() {
		return SIGNTP;
	}
	public void setSIGNTP(String sIGNTP) {
		SIGNTP = sIGNTP;
	}
	public String getSIGN() {
		return SIGN;
	}
	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}
}
