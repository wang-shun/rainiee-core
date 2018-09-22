package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * 使用场景
 */
public enum T6335_F02{


    /** 
     * 投资
     */
    TZ("投资");

    protected final String chineseName;

    private T6335_F02(String chineseName){
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
     * @return {@link T6335_F02}
     */
    public static final T6335_F02 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6335_F02.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
