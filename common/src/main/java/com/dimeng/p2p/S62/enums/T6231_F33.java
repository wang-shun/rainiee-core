package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/** 
 * 允许投资的终端
 */
public enum T6231_F33
{
    /** 
     * PC_APP
     */
    PC_APP("PC+APP"),

    /** 
     * PC
     */
    PC("PC"),
    
    /** 
     * PC
     */
    APP("APP");

    protected final String chineseName;

    private T6231_F33(String chineseName){
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
     * @return {@link T6231_F33}
     */
    public static final T6231_F33 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6231_F33.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
