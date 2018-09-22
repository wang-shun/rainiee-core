package com.dimeng.p2p.pay.service.fuyou.entity;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.dimeng.p2p.pay.service.fuyou.util.FuYouMD5;
import com.dimeng.p2p.pay.service.fuyou.util.XmlBeanUtils;

@XmlRootElement(name="ORDER")
public class OrderQryByMSsn
{
	private String version;
	
	private String mchntCd;
	
	private String mchntOrderId;
	
	private String rem1;
	
	private String rem2;
	
	private String rem3;
	
	private String sign;

	public String getVersion()
	{
		return version;
	}

	@XmlElement(name="VERSION")
	public void setVersion(String version)
	{
		this.version = version;
	}

	public String getMchntCd()
	{
		return mchntCd;
	}
	@XmlElement(name="MCHNTCD")
	public void setMchntCd(String mchntCd)
	{
		this.mchntCd = mchntCd;
	}

	public String getMchntOrderId()
	{
		return mchntOrderId;
	}
	@XmlElement(name="MCHNTORDERID")
	public void setMchntOrderId(String mchntOrderId)
	{
		this.mchntOrderId = mchntOrderId;
	}
	
	public String getRem1()
	{
		return rem1;
	}
	@XmlElement(name="REM1")
	public void setRem1(String rem1)
	{
		this.rem1 = rem1;
	}
	
	public String getRem2()
	{
		return rem2;
	}
	@XmlElement(name="REM2")
	public void setRem2(String rem2)
	{
		this.rem2 = rem2;
	}
	
	public String getRem3()
	{
		return rem3;
	}
	
	@XmlElement(name="REM3")
	public void setRem3(String rem3)
	{
		this.rem3 = rem3;
	}

	public String getSign()
	{
		return sign;
	}
	@XmlElement(name="SIGN")
	public void setSign(String sign)
	{
		this.sign = sign;
	}
	
	public String buildXml(String mchntKey) throws JAXBException{
		StringBuilder temp = new StringBuilder();
		temp.append(this.version).append("|").append(this.mchntCd).append("|").append(this.mchntOrderId).append("|").append(mchntKey);
        this.sign = FuYouMD5.MD5Encode(temp.toString());
        return XmlBeanUtils.convertBean2Xml(this, "UTF-8", false);
	}
}
