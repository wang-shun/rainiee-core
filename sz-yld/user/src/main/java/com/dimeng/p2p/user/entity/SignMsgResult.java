package com.dimeng.p2p.user.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 协议绑定短信验证码请求响应信息实体类
 * @author raoyujun
 *
 */
@XmlRootElement(name = "RESPONSE")
public class SignMsgResult {
	
	private String VERSION;  // 响应码
	private String RESPONSECODE; // 响应描述
	private String RESPONSEMSG;
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
	public String getMCHNTSSN() {
		return MCHNTSSN;
	}
	public void setMCHNTSSN(String mCHNTSSN) {
		MCHNTSSN = mCHNTSSN;
	}	

	


}
