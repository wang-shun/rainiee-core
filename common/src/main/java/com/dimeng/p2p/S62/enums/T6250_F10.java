package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否属于融资担保
 */
public enum T6250_F10{


    /** 
     * 否
     */
    F("否"),

    /** 
     * 是
     */
    S("是");

    protected final String chineseName;

    private T6250_F10(String chineseName){
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
     * @return {@link T6250_F10}
     */
    public static final T6250_F10 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6250_F10.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
