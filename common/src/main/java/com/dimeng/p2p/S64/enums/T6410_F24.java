package com.dimeng.p2p.S64.enums;


import com.dimeng.util.StringHelper;

/** 
 * 保障方式
 */
public enum T6410_F24{


    /** 
     * 全额本息保障
     */
    QEBXBZ("全额本息保障");

    protected final String chineseName;

    private T6410_F24(String chineseName){
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
     * @return {@link T6410_F24}
     */
    public static final T6410_F24 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6410_F24.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
