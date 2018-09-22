package com.dimeng.p2p.modules.nciic.service.entity;

import java.io.Serializable;

public class QueryResult implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    public Identifier Identifier;
    
    public String RawXml;
    
    public String ResponseCode;
    
    public String ResponseText;
}
