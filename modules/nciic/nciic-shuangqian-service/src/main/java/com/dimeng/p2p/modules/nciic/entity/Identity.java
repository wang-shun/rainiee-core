package com.dimeng.p2p.modules.nciic.entity;

import java.io.Serializable;

/*
 * 文 件 名:  Identity.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  YINKE
 * 修改时间:  2015年4月9日
 */
public class Identity implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 真实姓名
     */
    private String realName;
    
    /**
     * 身份证号码
     */
    private String identificationNo;
    
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
    
    @Override
    public String toString()
    {
        return "Identity [realName=" + realName + ", identificationNo=" + identificationNo + "]";
    }
}
