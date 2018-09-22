package com.dimeng.p2p.S71.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否需要修改密码
 */
public enum T7110_F09{


    /** 
     * 否
     */
    F("否"),

    /** 
     * 是
     */
    S("是");

    protected final String chineseName;

    private T7110_F09(String chineseName){
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
     * @return {@link T7110_F09}
     */
    public static final T7110_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T7110_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
