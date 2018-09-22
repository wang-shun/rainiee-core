package com.dimeng.p2p.pay.service.fuyou.entity;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 充值响应信息实体类
 * @author raoyujun
 *
 */
@XmlRootElement(name = "ap")
public class ChargeAp {
	
    private Plain plain;   
    private String md5;
    
    @XmlElement(name = "plain")
	public Plain getPlain() {
		return plain;
	}
	public void setPlain(Plain plain) {
		this.plain = plain;
	}
	public String getMd5() {
		return md5;
	}
	public void setMd5(String md5) {
		this.md5 = md5;
	}    
   

}
