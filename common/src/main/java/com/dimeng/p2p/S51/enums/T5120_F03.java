package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T5120_F03{


    /** 
     * 停用
     */
    TY("停用"),

    /** 
     * 启用
     */
    QY("启用");

    protected final String chineseName;

    private T5120_F03(String chineseName){
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
     * @return {@link T5120_F03}
     */
    public static final T5120_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5120_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
