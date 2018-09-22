package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 注册来源
 */
public enum T6110_F08{


    /** 
     * PC注册
     */
    PCZC("PC注册"),
    
    /** 
     * APP注册
     */
    APPZC("APP注册"),
    
    /** 
     * 微信注册
     */
    WXZC("微信注册"),

    /** 
     * 后台添加
     */
    HTTJ("后台添加");

    protected final String chineseName;

    private T6110_F08(String chineseName){
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
     * @return {@link T6110_F08}
     */
    public static final T6110_F08 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6110_F08.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
