package com.dimeng.p2p.S61.enums;

import com.dimeng.util.StringHelper;

public enum T6118_F10 {

    /** 
     * 通过
     */
    TG("通过"),

    /** 
     * 不通过
     */
    BTG("不通过");

    protected final String chineseName;

    private T6118_F10(String chineseName){
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
     * @return {@link T6118_F10}
     */
    public static final T6118_F10 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6118_F10.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
