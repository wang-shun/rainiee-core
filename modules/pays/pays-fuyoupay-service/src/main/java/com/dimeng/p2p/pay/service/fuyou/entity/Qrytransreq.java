package com.dimeng.p2p.pay.service.fuyou.entity;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "qrytransreq")
@XmlType(propOrder = { "ver", "busicd", "orderno", "startdt", "enddt", "transst" })
public class Qrytransreq {
	
	private String ver;	         // 版本号
	private String busicd;	     // 业务代码  代收（AC01）,代付（AP01）,退票（TP01）
	private String orderno;	     // 原请求流水  是代收、代付中接口的orderno
	private String startdt;	     // 开始日期  否
	private String enddt;	     // 结束日期  否日期段不能超过15 天
	private String transst;	     // 交易状态  是参见交易状态码
	public String getVer() {
		return ver;
	}
	public void setVer(String ver) {
		this.ver = ver;
	}
	public String getBusicd() {
		return busicd;
	}
	public void setBusicd(String busicd) {
		this.busicd = busicd;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getStartdt() {
		return startdt;
	}
	public void setStartdt(String startdt) {
		this.startdt = startdt;
	}
	public String getEnddt() {
		return enddt;
	}
	public void setEnddt(String enddt) {
		this.enddt = enddt;
	}
	public String getTransst() {
		return transst;
	}
	public void setTransst(String transst) {
		this.transst = transst;
	}

}
