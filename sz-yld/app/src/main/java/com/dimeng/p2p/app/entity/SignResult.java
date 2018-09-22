package com.dimeng.p2p.app.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 协议支付绑定响应信息实体类
 * @author raoyujun
 *
 */
@XmlRootElement(name = "RESPONSE")
public class SignResult {
	
	private String VERSION;  // 响应码
	private String RESPONSECODE; // 响应描述
	private String RESPONSEMSG;
	private String PROTOCOLNO;
	private String MCHNTSSN;
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
	public String getPROTOCOLNO() {
		return PROTOCOLNO;
	}
	public void setPROTOCOLNO(String pROTOCOLNO) {
		PROTOCOLNO = pROTOCOLNO;
	}
	public String getMCHNTSSN() {
		return MCHNTSSN;
	}
	public void setMCHNTSSN(String mCHNTSSN) {
		MCHNTSSN = mCHNTSSN;
	}	

	


}
