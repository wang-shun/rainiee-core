package com.dimeng.p2p.modules.nciic.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("data")
public class Data implements java.io.Serializable
{
    @XStreamAlias("message")
    private Message message;
    
    @XStreamAlias("policeCheckInfos")
    private PoliceCheckInfos policeCheckInfos;
    
    public Message getMessage()
    {
        return message;
    }
    
    public void setMessage(Message message)
    {
        this.message = message;
    }
    
    public PoliceCheckInfos getPoliceCheckInfos()
    {
        return policeCheckInfos;
    }
    
    public void setPoliceCheckInfos(PoliceCheckInfos policeCheckInfos)
    {
        this.policeCheckInfos = policeCheckInfos;
    }
    
}
