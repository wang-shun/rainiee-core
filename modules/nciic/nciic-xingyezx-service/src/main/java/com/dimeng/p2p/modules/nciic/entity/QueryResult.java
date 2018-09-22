package com.dimeng.p2p.modules.nciic.entity;

import javax.xml.bind.annotation.XmlRootElement;

/*
 * 文 件 名:  QueryResult.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年10月21日
 */
@XmlRootElement(name = "queryResult")
public class QueryResult
{
    private String name;
    
    private String certType;
    
    private String certNo;
    
    private String telephoneNo;
    
    private String dataSourceNo;
    
    private String interfaceNo;
    
    private String version;
    
    private String taskId;
    
    private String respCode;
    
    private String respMessage;
    
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
    
    public String getTelephoneNo()
    {
        return telephoneNo;
    }
    
    public void setTelephoneNo(String telephoneNo)
    {
        this.telephoneNo = telephoneNo;
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
    
    public String getTaskId()
    {
        return taskId;
    }
    
    public void setTaskId(String taskId)
    {
        this.taskId = taskId;
    }
    
    public String getRespCode()
    {
        return respCode;
    }
    
    public void setRespCode(String respCode)
    {
        this.respCode = respCode;
    }
    
    public String getRespMessage()
    {
        return respMessage;
    }
    
    public void setRespMessage(String respMessage)
    {
        this.respMessage = respMessage;
    }
    
}
