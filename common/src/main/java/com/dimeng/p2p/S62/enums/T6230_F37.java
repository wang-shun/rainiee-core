package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否已项目登记
 */
public enum T6230_F37{


    /** 
     * 否
     */
    F("否"),

    /** 
     * 是
     */
    S("是");

    protected final String chineseName;

    private T6230_F37(String chineseName){
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
     * @return {@link T6230_F37}
     */
    public static final T6230_F37 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6230_F37.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
