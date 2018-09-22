package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否是手机号作为邀请码
 */
public enum T6111_F06{

    F("F"),
    S("S");

    protected final String chineseName;

    private T6111_F06(String chineseName){
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
     * @return {@link T6111_F06}
     */
    public static final T6111_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6111_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
