package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否第一次登陆系统
 */
public enum T6110_F12{


    /** 
     * 否
     */
    F("否"),

    /** 
     * 是
     */
    S("是");

    protected final String chineseName;

    private T6110_F12(String chineseName){
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
     * @return {@link T6110_F12}
     */
    public static final T6110_F12 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6110_F12.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
