package com.dimeng.p2p.modules.nciic.service.entity;

/**
 * 身份认证查询结果
 * @author wangshaohua
 *
 */
public class IdentityAuthQueryResult
{
    
    public INFO info;
    
    public VALIDRET validret;
    
    public INFO getInfo()
    {
        return info;
    }
    
    public void setInfo(INFO info)
    {
        this.info = info;
    }
    
    public VALIDRET getValidret()
    {
        return validret;
    }
    
    public void setValidret(VALIDRET validret)
    {
        this.validret = validret;
    }
    
}
