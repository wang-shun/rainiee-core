package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否实地认证
 */
public enum T6281_F19{


    /** 
     * 是
     */
    S("是"),

    /** 
     * 否
     */
    F("否");

    protected final String chineseName;

    private T6281_F19(String chineseName){
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
     * @return {@link T6281_F19}
     */
    public static final T6281_F19 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6281_F19.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
