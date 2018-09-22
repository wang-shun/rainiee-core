/*
 * 文 件 名:  T6290_F04.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年8月18日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * <HKTX:还款提醒、YQTX:逾期提醒>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年8月18日]
 */
public enum T6290_F04
{
    /** 
     * 未处理
     */
    HKTX("还款提醒"),
    
    /** 
     * 逾期提醒
     */
    YQTX("逾期提醒");
    
    protected final String chineseName;
    
    private T6290_F04(String chineseName)
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
     * @return {@link T6290_F04}
     */
    public static final T6290_F04 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6290_F04.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
