/*
 * 文 件 名:  T6289_F09.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月13日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [v3.1.2, 2015年10月13日]
 */
public enum T6289_F09
{
    WFH("未返还"), YFH("已返还"), YSX("已失效");
    
    protected final String chineseName;
    
    private T6289_F09(String chineseName)
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
     * @return {@link T6289_F09}
     */
    public static final T6289_F09 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6289_F09.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
    
}
