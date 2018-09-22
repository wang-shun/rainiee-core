package com.dimeng.p2p.modules.nciic.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("message")
public class Message implements java.io.Serializable
{
    @XStreamAlias("status")
    private String status;
    
    @XStreamAlias("value")
    private String value;
    
    public String getStatus()
    {
        return status;
    }
    
    public void setStatus(String status)
    {
        this.status = status;
    }
    
    public String getValue()
    {
        return value;
    }
    
    public void setValue(String value)
    {
        this.value = value;
    }
    
}
