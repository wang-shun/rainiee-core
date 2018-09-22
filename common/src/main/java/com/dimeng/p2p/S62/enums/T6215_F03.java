package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6215_F03{

    S("S"),
    F("F");

    protected final String chineseName;

    private T6215_F03(String chineseName){
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
     * @return {@link T6215_F03}
     */
    public static final T6215_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6215_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
