/*
 * 文 件 名:  T6271_F10.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月25日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月25日]
 */
public enum T6271_F10
{
    TZR("投资人"), JKR("借款人"), ZCR("转出人"), SRR("受让人"), DFR("垫付人"), BDFR("被垫付人"), DBR("担保人");
    
    protected final String chineseName;
    
    private T6271_F10(String chineseName)
    {
        this.chineseName = chineseName;
    }
    
    public String getChineseName()
    {
        return chineseName;
    }
    
    public static final T6271_F10 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6271_F10.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
