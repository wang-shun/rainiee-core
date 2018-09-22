package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/** 
 * 筛选类型
 */
public enum T6299_F04
{
    
    /**
     * 借款期限
     */
    JKQX("借款期限"),
    
    /**
     * 融资进度
     */
    RZJD("融资进度"),
    
    /**
     * 年化利率
     */
    NHSY("年化利率");
    
    protected final String chineseName;
    
    private T6299_F04(String chineseName)
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
     * @return {@link T6299_F04}
     */
    public static final T6299_F04 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6299_F04.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
