package com.dimeng.p2p.modules.nciic.service.entity;

import java.io.Serializable;

public class INFO implements Serializable
{
    /**
     * 交易代码
     */
    String TRX_CODE;
    
    /**
     * 版本
     */
    String VERSION;
    
    /**
     * 数据格式
     */
    String DATA_TYPE;
    
    /**
     * 处理级别
     */
    String LEVEL;
    
    /**
     * 商户号
     */
    String MERCHANT_ID;
    
    /**
     * 用户名
     */
    String USER_NAME;
    
    /**
     * 用户密码
     */
    String USER_PASS;
    
    /**
     * 交易批次号
     */
    String REQ_SN;
    
    /**
     * 返回代码
     */
    String RET_CODE;
    
    /**
     * 错误信息
     */
    String ERR_MSG;
    
    /**
     * 签名信息
     */
    String SIGNED_MSG;
    
    public String getTRX_CODE()
    {
        return TRX_CODE;
    }
    
    public void setTRX_CODE(String tRX_CODE)
    {
        TRX_CODE = tRX_CODE;
    }
    
    public String getVERSION()
    {
        return VERSION;
    }
    
    public void setVERSION(String vERSION)
    {
        VERSION = vERSION;
    }
    
    public String getDATA_TYPE()
    {
        return DATA_TYPE;
    }
    
    public void setDATA_TYPE(String dATA_TYPE)
    {
        DATA_TYPE = dATA_TYPE;
    }
    
    public String getLEVEL()
    {
        return LEVEL;
    }
    
    public void setLEVEL(String lEVEL)
    {
        LEVEL = lEVEL;
    }
    
    public String getMERCHANT_ID()
    {
        return MERCHANT_ID;
    }
    
    public void setMERCHANT_ID(String mERCHANT_ID)
    {
        MERCHANT_ID = mERCHANT_ID;
    }
    
    public String getUSER_NAME()
    {
        return USER_NAME;
    }
    
    public void setUSER_NAME(String uSER_NAME)
    {
        USER_NAME = uSER_NAME;
    }
    
    public String getUSER_PASS()
    {
        return USER_PASS;
    }
    
    public void setUSER_PASS(String uSER_PASS)
    {
        USER_PASS = uSER_PASS;
    }
    
    public String getREQ_SN()
    {
        return REQ_SN;
    }
    
    public void setREQ_SN(String rEQ_SN)
    {
        REQ_SN = rEQ_SN;
    }
    
    public String getSIGNED_MSG()
    {
        return SIGNED_MSG;
    }
    
    public void setSIGNED_MSG(String sIGNED_MSG)
    {
        SIGNED_MSG = sIGNED_MSG;
    }
    
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
