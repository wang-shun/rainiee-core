/*
 * 文 件 名:  T6271_F08.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月17日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * <一句话功能简述> 合同类型枚举类
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月17日]
 */
public enum T6271_F08
{
    
    JKHT("借款合同"), ZQZRHT("债权转让合同"), DFHT("垫付合同"), BLZQZRHT("不良债权转让合同"), DBHT("担保合同");
    
    protected final String chineseName;
    
    private T6271_F08(String chineseName)
    {
        this.chineseName = chineseName;
    }
    
    public String getChineseName()
    {
        return chineseName;
    }
    
    public static final T6271_F08 parse(String value)
    {
        if (StringHelper.isEmpty(value))
        {
            return null;
        }
        try
        {
            return T6271_F08.valueOf(value);
        }
        catch (Throwable t)
        {
            return null;
        }
    }
}
