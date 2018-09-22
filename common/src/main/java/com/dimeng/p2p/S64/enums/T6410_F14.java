package com.dimeng.p2p.S64.enums;


import com.dimeng.util.StringHelper;

/** 
 * 收益处理
 */
public enum T6410_F14{


    /** 
     * 等额本息
     */
    DEBX("等额本息"),

    /** 
     * 每月还息
     */
    MYHXDQHB("每月还息"),

    /** 
     * 一次性还本付息
     */
    YCXHBFX("一次性还本付息");

    protected final String chineseName;

    private T6410_F14(String chineseName){
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
     * @return {@link T6410_F14}
     */
    public static final T6410_F14 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6410_F14.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
