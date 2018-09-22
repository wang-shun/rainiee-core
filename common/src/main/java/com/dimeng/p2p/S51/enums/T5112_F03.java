package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 客服类型
 */
public enum T5112_F03{


    /** 
     * 邮箱
     */
    YX("邮箱"),

    /** 
     * 电话
     */
    DH("电话"),

    /** 
     * QQ
     */
    QQ("QQ");

    protected final String chineseName;

    private T5112_F03(String chineseName){
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
     * @return {@link T5112_F03}
     */
    public static final T5112_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5112_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
