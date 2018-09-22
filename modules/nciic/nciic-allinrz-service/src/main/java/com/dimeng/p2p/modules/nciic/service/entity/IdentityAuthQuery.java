package com.dimeng.p2p.modules.nciic.service.entity;

/**
 * 身份认证查询
 * @author wangshaohua
 *
 */
public class IdentityAuthQuery
{
    public INFO iNFO;
    
    public VERQRY vERQRY;
    
    public INFO getiNFO()
    {
        return iNFO;
    }
    
    public void setiNFO(INFO iNFO)
    {
        this.iNFO = iNFO;
    }
    
    public VERQRY getvERQRY()
    {
        return vERQRY;
    }
    
    public void setvERQRY(VERQRY vERQRY)
    {
        this.vERQRY = vERQRY;
    }
    
}
