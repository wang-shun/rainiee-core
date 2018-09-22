package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 必要认证
 */
public enum T5123_F03{


    /** 
     * 是
     */
    S("是"),

    /** 
     * 否
     */
    F("否");

    protected final String chineseName;

    private T5123_F03(String chineseName){
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
     * @return {@link T5123_F03}
     */
    public static final T5123_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5123_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
