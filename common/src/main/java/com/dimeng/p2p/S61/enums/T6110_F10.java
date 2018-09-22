package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 担保方
 */
public enum T6110_F10{


    /** 
     * 是
     */
    S("是"),

    /** 
     * 否
     */
    F("否");

    protected final String chineseName;

    private T6110_F10(String chineseName){
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
     * @return {@link T6110_F10}
     */
    public static final T6110_F10 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6110_F10.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
