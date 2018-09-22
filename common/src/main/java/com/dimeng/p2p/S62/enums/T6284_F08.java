package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 处理状态
 */
public enum T6284_F08{


    /** 
     * 未处理
     */
    WCL("未处理"),

    /** 
     * 已处理
     */
    YCL("已处理");

    protected final String chineseName;

    private T6284_F08(String chineseName){
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
     * @return {@link T6284_F08}
     */
    public static final T6284_F08 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6284_F08.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
