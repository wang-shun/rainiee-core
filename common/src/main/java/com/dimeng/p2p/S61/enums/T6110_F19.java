/*
 * 文 件 名:  T6110_F19.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月27日
 */
package com.dimeng.p2p.S61.enums;

import com.dimeng.util.StringHelper;

/**
 * <是否允许购买不良债权>
 * <功能详细描述>
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月27日]
 */
public enum T6110_F19
{
    S("是"), F("否");
    
    protected final String chineseName;
    
    private T6110_F19(String chineseName)
    {
        this.chineseName = chineseName;
    }
    
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    public String getChineseName()
    {
        return chineseName;
    }
    
    /**
     * 解析字符串.
     * 
     * @return {@link T6110_F19}
     */
    public static final T6110_F19 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6110_F19.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
