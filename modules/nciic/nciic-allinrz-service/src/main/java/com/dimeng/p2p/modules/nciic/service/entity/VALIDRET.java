package com.dimeng.p2p.modules.nciic.service.entity;

import java.io.Serializable;

public class VALIDRET implements Serializable
{
    
    /**
     * 返回码
     */
    String RET_CODE;
    
    /**
     * 错误文本
     */
    String ERR_MSG;
    
    public String getRET_CODE()
    {
        return RET_CODE;
    }
    
    public void setRET_CODE(String rET_CODE)
    {
        RET_CODE = rET_CODE;
    }
    
    public String getERR_MSG()
    {
        return ERR_MSG;
    }
    
    public void setERR_MSG(String eRR_MSG)
    {
        ERR_MSG = eRR_MSG;
    }
    
}
