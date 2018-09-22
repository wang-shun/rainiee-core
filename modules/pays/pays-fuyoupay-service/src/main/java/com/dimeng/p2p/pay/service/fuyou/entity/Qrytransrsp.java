package com.dimeng.p2p.pay.service.fuyou.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 代付响应信息实体类
 * @author raoyujun
 *
 */
@XmlRootElement(name = "qrytransrsp")
public class Qrytransrsp {
	
	private String ret;  // 响应码
	private String memo; // 响应描述
	private Trans trans;
	
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Trans getTrans() {
		return trans;
	}
	public void setTrans(Trans trans) {
		this.trans = trans;
	}

}
