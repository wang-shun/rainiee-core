package com.dimeng.p2p.S71.enums;


import com.dimeng.util.StringHelper;

/** 
 * 发送对象
 */
public enum T7164_F07{


    /** 
     * 所有
     */
    SY("所有"),

    /** 
     * 指定人
     */
    ZDR("指定人");

    protected final String chineseName;

    private T7164_F07(String chineseName){
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
     * @return {@link T7164_F07}
     */
    public static final T7164_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T7164_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
