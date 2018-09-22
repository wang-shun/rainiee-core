/*
 * 文 件 名:  T6242_F11.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 公益标的状态
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public enum T6242_F11
{
    /** 
     * 申请中
     */
    SQZ("申请中"),

    /** 
     * 待审核
     */
    DSH("待审核"),

    /** 
     * 待发布
     */
    DFB("待发布"),

    /** 
     * 捐款中
     */
    JKZ("捐款中"),

    /** 
     * 已捐助
     */
    YJZ("已捐助"),
    
    /** 
     * 作废
     */
    YZF("已作废");

    protected final String chineseName;

    private T6242_F11(String chineseName){
        this.chineseName = chineseName;
    }
    /**
     * 获取中文名称.
     * 
     * @return {@link String}
     */
    public String getChineseName() {
        return chineseName;
    }
    /**
     * 解析字符串.
     * 
     * @return {@link T6242_F11}
     */
    public static final T6242_F11 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6242_F11.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
