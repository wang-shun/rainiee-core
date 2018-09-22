/*
 * 文 件 名:  T6231_F36
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  活动使用方式(奖励：红包、加息券、体验金)
 * 修 改 人:  heluzhu
 * 修改时间: 2016/8/18
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 活动使用方式(奖励：红包、加息券、体验金)
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/8/18]
 */
public enum T6231_F36 {
    /**
     * 组合使用
     */
    ALL("组合使用"),
    /**
     * 单一使用
     */
    SINGLE("单一使用"),
    /**
     * 不使用
     */
    NONE("不使用");

    protected final String chineseName;

    private T6231_F36(String chineseName)
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
    public static final T6231_F36 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6231_F36.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
