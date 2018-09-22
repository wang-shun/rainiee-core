package com.dimeng.p2p.S62.enums;

/*
 * 文 件 名:  T6230_Ext_F04.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  是否开启授权枚举
 * 修 改 人:  wangming
 * 修改时间:  2014年12月25日
 */
public enum T6230_Ext_F04
{
    F("未开启自动还款授权"), S("开启自动还款授权");
    private String description;
    
    private T6230_Ext_F04(String decription)
    {
        this.description = decription;
    }
    
    public String getDescription()
    {
        return description;
    }
    
    public void setDescription(String description)
    {
        this.description = description;
    }
    
    public static T6230_Ext_F04 parse(String name)
    {
        if (T6230_Ext_F04.F.name().equals(name))
        {
            return T6230_Ext_F04.F;
        }
        else if (T6230_Ext_F04.S.name().equals(name))
        {
            return T6230_Ext_F04.S;
        }
        return null;
    }
}
