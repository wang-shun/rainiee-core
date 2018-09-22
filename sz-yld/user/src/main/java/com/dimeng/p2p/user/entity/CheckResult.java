package com.dimeng.p2p.user.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 协议支付订单结果查询响应信息实体类
 * @author raoyujun
 *
 */
@XmlRootElement(name = "RESPONSE")
public class CheckResult {
	
	private String VERSION;  // 版本号
	private String RESPONSECODE; // 响应吗
	private String RESPONSEMSG; //响应中文描述
	private String MCHNTORDERID;//商户流水号
	private String AMT; //交易金额
	private String ORDERDATE; //交易日期
	private String BANKCARD; //银行卡号
	private String SIGN; //MD5 摘要数据
	public String getVERSION() {
		return VERSION;
	}
	public void setVERSION(String vERSION) {
		VERSION = vERSION;
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
	public String getMCHNTORDERID() {
		return MCHNTORDERID;
	}
	public void setMCHNTORDERID(String mCHNTORDERID) {
		MCHNTORDERID = mCHNTORDERID;
	}
	public String getAMT() {
		return AMT;
	}
	public void setAMT(String aMT) {
		AMT = aMT;
	}
	public String getORDERDATE() {
		return ORDERDATE;
	}
	public void setORDERDATE(String oRDERDATE) {
		ORDERDATE = oRDERDATE;
	}
	public String getBANKCARD() {
		return BANKCARD;
	}
	public void setBANKCARD(String bANKCARD) {
		BANKCARD = bANKCARD;
	}
	public String getSIGN() {
		return SIGN;
	}
	public void setSIGN(String sIGN) {
		SIGN = sIGN;
	}
	
}
