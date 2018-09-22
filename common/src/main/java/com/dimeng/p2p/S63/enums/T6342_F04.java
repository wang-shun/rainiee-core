/*
 * 文 件 名:  T6342_F04.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年9月30日
 */
package com.dimeng.p2p.S63.enums;

import com.dimeng.util.StringHelper;

/**
 * 活动中：加息券，红包使用状态
 * [WSY("未使用"), YSY("已使用"), YGQ("已过期")]
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年9月30日]
 */
public enum T6342_F04
{
    WSY("未使用"), YSY("已使用"), YGQ("已过期");
    
    protected final String chineseName;
    
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    private T6342_F04(String chineseName)
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
     * @return {@link T6342_F04}
     */
    public static final T6342_F04 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6342_F04.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
