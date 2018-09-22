package com.dimeng.p2p.S64.enums;


import com.dimeng.util.StringHelper;

/** 
 * 计划状态
 */
public enum T6410_F07{


    /** 
     * 新建
     */
    XJ("新建"),

    /** 
     * 已发布
     */
    YFB("已发布"),

    /** 
     * 已生效
     */
    YSX("已生效"),

    /** 
     * 已截止
     */
    YJZ("已截止");

    protected final String chineseName;

    private T6410_F07(String chineseName){
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
     * @return {@link T6410_F07}
     */
    public static final T6410_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6410_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
