package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/** 
 * 来源渠道
 */
public enum T6262_F11 {

	/** 
     * PC
     */
    PC("PC"),

    /** 
     * APP
     */
    APP("APP"),
    
    /** 
     * WEIXIN
     */
    WEIXIN("微信");

    protected final String chineseName;

    private T6262_F11(String chineseName){
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
     * @return {@link T6262_F11}
     */
    public static final T6262_F11 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6262_F11.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
    
}
