package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6231_F32{


    /** 
     * 启用
     */
    QY("启用"),

    /** 
     * 停用
     */
    TY("停用");

    protected final String chineseName;

    private T6231_F32(String chineseName){
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
     * @return {@link T6231_F32}
     */
    public static final T6231_F32 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6231_F32.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
