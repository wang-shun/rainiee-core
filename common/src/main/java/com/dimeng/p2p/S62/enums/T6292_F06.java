package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否取消
 */
public enum T6292_F06 {


    /**
     * 否
     */
    F("否"),

    /**
     * 是
     */
    S("是");

    protected final String chineseName;

    private T6292_F06(String chineseName){
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
     * @return {@link T6292_F06}
     */
    public static final T6292_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6292_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
