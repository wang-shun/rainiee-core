package com.dimeng.p2p.modules.nciic.entity;

import com.thoughtworks.xstream.annotations.XStreamAlias;

@SuppressWarnings("serial")
@XStreamAlias("policeCheckInfos")
public class PoliceCheckInfos implements java.io.Serializable
{
    @XStreamAlias("policeCheckInfo")
    private PoliceCheckInfo policeCheckInfo;
    
    public PoliceCheckInfo getPoliceCheckInfo()
    {
        return policeCheckInfo;
    }
    
    public void setPoliceCheckInfo(PoliceCheckInfo policeCheckInfo)
    {
        this.policeCheckInfo = policeCheckInfo;
    }
    
}
