package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 来源
 */
public enum T6103_F08{

    /** 
     * 注册
     */
    ZC("注册"),

    /**
     * 平台赠送
     */
    PTZS("平台赠送");

    protected final String chineseName;

    private T6103_F08(String chineseName){
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
     * @return {@link T6103_F08}
     */
    public static final T6103_F08 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6103_F08.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
