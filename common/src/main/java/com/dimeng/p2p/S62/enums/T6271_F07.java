/*
 * 文 件 名:  T6271_F07.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月16日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * <合同保全状态：WBQ-未保全，YBQ-已保全>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2016年6月16日]
 */
public enum T6271_F07
{
    WBQ("未保全"), YBQ("已保全");
    
    protected final String chineseName;
    
    private T6271_F07(String chineseName)
    {
        this.chineseName = chineseName;
    }
    
    public String getChineseName()
    {
        return chineseName;
    }
    
    public static final T6271_F07 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6271_F07.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
