package com.dimeng.p2p.user.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 协议支付解约响应信息实体类
 * @author raoyujun
 *
 */
@XmlRootElement(name = "RESPONSE")
public class TerminationResult {
	
	private String VERSION;  // 版本号
	private String RESPONSECODE; // 返回码
	private String RESPONSEMSG; //返回信息
	private String MCHNTCD;  //协议号
	private String USERID;   //流水号
	private String PROTOCOLNO;//协议号
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

}
