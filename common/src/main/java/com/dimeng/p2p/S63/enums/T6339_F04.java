package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * 兑换状态
 */
public enum T6339_F04{


    /** 
     * 已兑换
     */
    YDH("已兑换"),

    /** 
     * 未兑换
     */
    WDH("未兑换");

    protected final String chineseName;

    private T6339_F04(String chineseName){
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
     * @return {@link T6339_F04}
     */
    public static final T6339_F04 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6339_F04.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
