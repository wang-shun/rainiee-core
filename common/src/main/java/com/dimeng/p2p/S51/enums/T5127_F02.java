package com.dimeng.p2p.S51.enums;


import com.dimeng.util.StringHelper;

/** 
 * 等级类型
 */
public enum T5127_F02{


    /** 
     * 投资
     */
    TZ("投资"),

    /** 
     * 借款
     */
    JK("借款");

    protected final String chineseName;

    private T5127_F02(String chineseName){
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
     * @return {@link T5127_F02}
     */
    public static final T5127_F02 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T5127_F02.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
