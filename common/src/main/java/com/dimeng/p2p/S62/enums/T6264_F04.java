/*
 * 文 件 名:  T6264_F04.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月15日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * <不良债权状态>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月15日]
 */
public enum T6264_F04
{
    
    /**
     * DSH:待审核
     */
    DSH("待审核"),
    
    /**
     * ZRZ:转让中
     */
    ZRZ("转让中"),
    
    /**
     * YZR:已转让
     */
    YZR("已转让"),
    
    /**
     * YXJ:已下架
     */
    YXJ("已下架"),
    
    /**
     * ZRSB:转让失败
     */
    ZRSB("转让失败");
    
    protected final String chineseName;
    
    private T6264_F04(String chineseName)
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
     * @return {@link T6264_F04}
     */
    public static final T6264_F04 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6264_F04.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
