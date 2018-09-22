package com.dimeng.p2p.modules.nciic.service.entity;

public class VERQRY
{
    
    /**
     * 要查询的流水
     */
    String QSN;
    
    /**
     * 查询目标
     */
    String QTARGET;
    
    public String getQSN()
    {
        return QSN;
    }
    
    public void setQSN(String qSN)
    {
        QSN = qSN;
    }
    
    public String getQTARGET()
    {
        return QTARGET;
    }
    
    public void setQTARGET(String qTARGET)
    {
        QTARGET = qTARGET;
    }
    
}
