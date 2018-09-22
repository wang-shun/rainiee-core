package com.dimeng.p2p.modules.nciic.service.entity;

/**
 * 身份认证返回结果实体
 * @author wangshaohua
 *
 */
public class IdentityAuthResult
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
