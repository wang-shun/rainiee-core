package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/**
 * on
 */
public enum T6356_F04{

    on("on"),
    off("off");

    protected final String chineseName;

    private T6356_F04(String chineseName){
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
     * @return {@link T6356_F04}
     */
    public static final T6356_F04 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6356_F04.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
