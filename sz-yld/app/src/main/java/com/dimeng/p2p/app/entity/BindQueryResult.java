package com.dimeng.p2p.app.entity;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "RESPONSE")
public class BindQueryResult {
	private String VERSION;  // 版本号
	private String RESPONSECODE; //响应吗
	private String RESPONSEMSG ;//相应描述
	private String MCHNTCD; //商户号
	private String USERID; //用户id
	private String PROTOCOLNO ;//协议号
	private String CARDNO; //银行卡号
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
	public String getPROTOCOLNO() {
		return PROTOCOLNO;
	}
	public void setPROTOCOLNO(String pROTOCOLNO) {
		PROTOCOLNO = pROTOCOLNO;
	}
	public String getCARDNO() {
		return CARDNO;
	}
	public void setCARDNO(String cARDNO) {
		CARDNO = cARDNO;
	}
}
