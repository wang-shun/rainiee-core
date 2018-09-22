package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6254_F07{


    /** 
     * 未还
     */
    WH("未还"),

    /** 
     * 还款中
     */
    HKZ("还款中"),

    /** 
     * 已还
     */
    YH("已还");

    protected final String chineseName;

    private T6254_F07(String chineseName){
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
     * @return {@link T6254_F07}
     */
    public static final T6254_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6254_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
