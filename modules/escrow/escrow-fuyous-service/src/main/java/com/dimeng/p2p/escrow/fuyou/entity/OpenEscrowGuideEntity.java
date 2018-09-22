package com.dimeng.p2p.escrow.fuyou.entity;

import java.io.Serializable;

/**
 * 
 * 托管引导页管理实体
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年5月16日]
 */
public class OpenEscrowGuideEntity implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    private String realNameLabel;
    
    private String realName;
    
    private String identificationNoLabel;
    
    private String identificationNo;
    
    private String mobile;
    
    /**
     * 用户标识
     * ZRR
     * FZRR：再分 QY 或 JG
     */
    private String userTag;
    
    private String maxLength;
    
    public String getRealNameLabel()
    {
        return realNameLabel;
    }
    
    public void setRealNameLabel(String realNameLabel)
    {
        this.realNameLabel = realNameLabel;
    }
    
    public String getRealName()
    {
        return realName;
    }
    
    public void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    public String getIdentificationNoLabel()
    {
        return identificationNoLabel;
    }
    
    public void setIdentificationNoLabel(String identificationNoLabel)
    {
        this.identificationNoLabel = identificationNoLabel;
    }
    
    public String getIdentificationNo()
    {
        return identificationNo;
    }
    
    public void setIdentificationNo(String identificationNo)
    {
        this.identificationNo = identificationNo;
    }
    
    public String getMobile()
    {
        return mobile;
    }
    
    public void setMobile(String mobile)
    {
        this.mobile = mobile;
    }
    
    public String getUserTag()
    {
        return userTag;
    }
    
    public void setUserTag(String userTag)
    {
        this.userTag = userTag;
    }
    
    public String getMaxLength()
    {
        return maxLength;
    }
    
    public void setMaxLength(String maxLength)
    {
        this.maxLength = maxLength;
    }
}
