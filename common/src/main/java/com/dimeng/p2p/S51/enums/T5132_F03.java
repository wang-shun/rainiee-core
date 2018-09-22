/*
 * 文 件 名:  T5132_F03.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年5月19日
 */
package com.dimeng.p2p.S51.enums;

import com.dimeng.util.StringHelper;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年5月19日]
 */
public enum T5132_F03
{
    /** 
     * 启用
     */
    QY("启用"),
    
    /** 
     * 停用
     */
    TY("停用");
    
    protected final String chineseName;
    
    private T5132_F03(String chineseName)
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
     * @return {@link T5132_F03}
     */
    public static final T5132_F03 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T5132_F03.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
