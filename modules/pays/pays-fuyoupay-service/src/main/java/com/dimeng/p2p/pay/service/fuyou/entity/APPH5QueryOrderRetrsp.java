package com.dimeng.p2p.pay.service.fuyou.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
//XML文件中的根标识  
@XmlRootElement(name = "RESPONSE")
//控制JAXB 绑定类中属性和字段的排序  
@XmlType(propOrder = {"VERSION", "RESPONSECODE", "RESPONSEMSG", "MCHNTORDERID", "SIGN",})
public class APPH5QueryOrderRetrsp implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 订单id
	 */
    public String VERSION;
    
    /**
     * 订单id
     */
    public String RESPONSECODE;
    
    /**
     * 订单id
     */
    public String RESPONSEMSG;
    
    /**
     * 订单id
     */
    public String MCHNTORDERID;
    
    /**
     * 订单id
     */
    public String SIGN;
}
