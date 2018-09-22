package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否实地认证
 */
public enum T6282_F16{


    /** 
     * 是
     */
    S("是"),

    /** 
     * 否
     */
    F("否");

    protected final String chineseName;

    private T6282_F16(String chineseName){
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
     * @return {@link T6282_F16}
     */
    public static final T6282_F16 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6282_F16.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
