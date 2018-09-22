package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6250_F13{


    /** 
     * 启用
     */
    QY("启用"),

    /** 
     * 停用
     */
    TY("停用");

    protected final String chineseName;

    private T6250_F13(String chineseName){
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
     * @return {@link T6250_F13}
     */
    public static final T6250_F13 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6250_F13.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
