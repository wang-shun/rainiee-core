package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 对账状态
 */
public enum T6102_F10{


    /** 
     * 未对账
     */
    WDZ("未对账"),

    /** 
     * 已对账
     */
    YDZ("已对账");

    protected final String chineseName;

    private T6102_F10(String chineseName){
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
     * @return {@link T6102_F10}
     */
    public static final T6102_F10 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6102_F10.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
