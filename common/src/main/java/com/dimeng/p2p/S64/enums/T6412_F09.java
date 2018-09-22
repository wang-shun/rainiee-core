package com.dimeng.p2p.S64.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6412_F09{


    /** 
     * 未还
     */
    WH("未还"),

    /** 
     * 已还
     */
    YH("已还");

    protected final String chineseName;

    private T6412_F09(String chineseName){
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
     * @return {@link T6412_F09}
     */
    public static final T6412_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6412_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
