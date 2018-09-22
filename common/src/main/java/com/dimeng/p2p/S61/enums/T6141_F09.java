/*
 * 文 件 名:  T6141_F09.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月8日
 */
package com.dimeng.p2p.S61.enums;

import com.dimeng.util.StringHelper;

/**
 * <性别>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月8日]
 */
public enum T6141_F09
{
    
    F("女"), M("男");
    
    protected final String chineseName;
    
    private T6141_F09(String chineseName)
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
     * @return {@link T6141_F09}
     */
    public static final T6141_F09 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6141_F09.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
