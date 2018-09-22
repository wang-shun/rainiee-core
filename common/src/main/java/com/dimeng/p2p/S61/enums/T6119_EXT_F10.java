package com.dimeng.p2p.S61.enums;

import com.dimeng.util.StringHelper;

/**
 * 签约类型
 */
public enum T6119_EXT_F10
{
    
    /**
     * 已签约
     */
    S("已签约"),
    
    /**
     * 未签约
     */
    F("未签约"),
    /**
     * 失效， 发送短信后，未能及时签约成功的,签约短信一般是十分钟有效期
     */
    YSX("已失效"),
    /**
     * 解约，只对当时申请签约成功的信息
     */
    YJY("已解约");
    
    protected final String chineseName;
    
    private T6119_EXT_F10(String chineseName)
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
     * @return {@link T6119_EXT_F10}
     */
    public static final T6119_EXT_F10 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6119_EXT_F10.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
