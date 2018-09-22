package com.dimeng.p2p.modules.nciic.service.entity;

/**
 * 国民身份验证实体类
 * @author wangshaohua
 *
 */
public class IdentityAuthEntity
{
    
    public INFO iNFO;
    
    public IDVER iDVER;
    
    public INFO getiNFO()
    {
        return iNFO;
    }
    
    public void setiNFO(INFO iNFO)
    {
        this.iNFO = iNFO;
    }
    
    public IDVER getiDVER()
    {
        return iDVER;
    }
    
    public void setiDVER(IDVER iDVER)
    {
        this.iDVER = iDVER;
    }
    
}
