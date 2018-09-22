package com.dimeng.p2p.modules.nciic.entity;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * 文 件 名:  QueryEntity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月20日
 */
@XmlRootElement(name = "query")
public class QueryEntity
{
    /**
     * 被查询人姓名
     */
    private String name;
    
    /**
     * 被查询人证件类型
     */
    private String certType;
    
    /**
     * 被查询人证件号码
     */
    private String certNo;
    
    /**
     * 查询数据源编号
     */
    private String dataSourceNo;
    
    /**
     * 查询接口编号
     */
    private String interfaceNo;
    
    /**
     * 接口版本号
     */
    private String version;
    
    /**
     * 被查询人电话号码
     */
    private String telephoneNo;
    
    /**
     * 常用联系人电话
     */
    private String contactPersonTels;
    
    /**
     * 是否强制实时查询
     */
    private String isRealQuery;
    
    private String reserve5;
    
    private String reserve6;
    
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    public String getCertType()
    {
        return certType;
    }
    
    public void setCertType(String certType)
    {
        this.certType = certType;
    }
    
    public String getCertNo()
    {
        return certNo;
    }
    
    public void setCertNo(String certNo)
    {
        this.certNo = certNo;
    }
    
    public String getDataSourceNo()
    {
        return dataSourceNo;
    }
    
    public void setDataSourceNo(String dataSourceNo)
    {
        this.dataSourceNo = dataSourceNo;
    }
    
    public String getInterfaceNo()
    {
        return interfaceNo;
    }
    
    public void setInterfaceNo(String interfaceNo)
    {
        this.interfaceNo = interfaceNo;
    }
    
    public String getVersion()
    {
        return version;
    }
    
    public void setVersion(String version)
    {
        this.version = version;
    }
    
    public String getTelephoneNo()
    {
        return telephoneNo;
    }
    
    public void setTelephoneNo(String telephoneNo)
    {
        this.telephoneNo = telephoneNo;
    }
    
    public String getContactPersonTels()
    {
        return contactPersonTels;
    }
    
    public void setContactPersonTels(String contactPersonTels)
    {
        this.contactPersonTels = contactPersonTels;
    }
    
    public String getIsRealQuery()
    {
        return isRealQuery;
    }
    
    public void setIsRealQuery(String isRealQuery)
    {
        this.isRealQuery = isRealQuery;
    }
    
    public String getReserve5()
    {
        return reserve5;
    }
    
    public void setReserve5(String reserve5)
    {
        this.reserve5 = reserve5;
    }
    
    public String getReserve6()
    {
        return reserve6;
    }
    
    public void setReserve6(String reserve6)
    {
        this.reserve6 = reserve6;
    }
    
}
