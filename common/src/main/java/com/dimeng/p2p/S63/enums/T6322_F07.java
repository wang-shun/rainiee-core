package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * CZ
 */
public enum T6322_F07{


    /** 
     * 充值
     */
    CZ("充值"),

    /** 
     * 投资
     */
    TZ("投资");

    protected final String chineseName;

    private T6322_F07(String chineseName){
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
     * @return {@link T6322_F07}
     */
    public static final T6322_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6322_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
