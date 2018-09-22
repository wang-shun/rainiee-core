/*
 * 文 件 名:  T6110_F18.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月8日
 */
package com.dimeng.p2p.S61.enums;

import com.dimeng.util.StringHelper;

/**
 * <企业帐号是否开启过投资功能>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月8日]
 */
public enum T6110_F18
{
    F("否"), S("是");
    
    protected final String chineseName;
    
    private T6110_F18(String chineseName)
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
     * @return {@link T6110_F18}
     */
    public static final T6110_F18 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6110_F18.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
