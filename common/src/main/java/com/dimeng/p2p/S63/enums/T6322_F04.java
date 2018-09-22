package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * WSY
 */
public enum T6322_F04{


    /** 
     * 已过期
     */
    YGQ("已过期"),

    /** 
     * 未使用
     */
    WSY("未使用"),

    /** 
     * 已使用
     */
    YSY("已使用");

    protected final String chineseName;

    private T6322_F04(String chineseName){
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
     * @return {@link T6322_F04}
     */
    public static final T6322_F04 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6322_F04.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
