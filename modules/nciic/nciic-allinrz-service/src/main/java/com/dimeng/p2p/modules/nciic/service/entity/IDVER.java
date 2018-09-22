package com.dimeng.p2p.modules.nciic.service.entity;

import java.io.Serializable;

public class IDVER implements Serializable
{
    
    /**
     * 姓名
     */
    String NAME;
    
    /**
     * 身份证号
     */
    String IDNO;
    
    /**
     * 有效期
     */
    String VALIDATE;
    
    /**
     * 备注
     */
    String REMARK;
    
    public String getNAME()
    {
        return NAME;
    }
    
    public void setNAME(String nAME)
    {
        NAME = nAME;
    }
    
    public String getIDNO()
    {
        return IDNO;
    }
    
    public void setIDNO(String iDNO)
    {
        IDNO = iDNO;
    }
    
    public String getVALIDATE()
    {
        return VALIDATE;
    }
    
    public void setVALIDATE(String vALIDATE)
    {
        VALIDATE = vALIDATE;
    }
    
    public String getREMARK()
    {
        return REMARK;
    }
    
    public void setREMARK(String rEMARK)
    {
        REMARK = rEMARK;
    }
    
}
