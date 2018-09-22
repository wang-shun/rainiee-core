package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 协议类型
 */
public enum T5117_1_F01{


    /** 
     * 注册协议
     */
    ZCXY("注册协议"),

    /** 
     * 本息保障
     */
    BXBZ("本息保障");

    protected final String chineseName;

    private T5117_1_F01(String chineseName){
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
     * @return {@link T5117_1_F01}
     */
    public static final T5117_1_F01 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5117_1_F01.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
