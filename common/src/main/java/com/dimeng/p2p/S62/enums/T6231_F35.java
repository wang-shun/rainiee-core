/*
 * 文 件 名:  T6231_F35.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年7月12日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * <来源：信用贷、担保贷、后台新增>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年7月12日]
 */
public enum T6231_F35
{
    /** 
     * 信用贷
     */
    XYD("信用贷"),
    
    /** 
     * 担保贷
     */
    DBD("担保贷"),
    
    /** 
     * 后台新增
     */
    HTXZ("后台新增");
    
    protected final String chineseName;
    
    private T6231_F35(String chineseName)
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
     * @return {@link T6231_F35}
     */
    public static final T6231_F35 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6231_F35.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
