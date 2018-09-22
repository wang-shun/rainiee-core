package com.dimeng.p2p.modules.nciic.entity;

import java.io.Serializable;

/*
 * 文 件 名:  IdentityRet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  姓名匹配列表
 * 修 改 人:  YINKE
 * 修改时间:  2015年4月10日
 */
public class IdentityRet implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 姓名
     */
    private String realName;
    
    /**
     * 身份证号码
     */
    private String identificationNo;
    
    /**
     * 匹配状态
     * 0、匹配失败 
     * 1、匹配成功
     */
    private String state;
    
    public String getRealName()
    {
        return realName;
    }
    
    public void setRealName(String realName)
    {
        this.realName = realName;
    }
    
    public String getIdentificationNo()
    {
        return identificationNo;
    }
    
    public void setIdentificationNo(String identificationNo)
    {
        this.identificationNo = identificationNo;
    }
    
    public String getState()
    {
        return state;
    }
    
    public void setState(String state)
    {
        this.state = state;
    }
}
