package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * 友金币获取方式
 */
public enum T6337_F03{


    /** 
     * 安全认证
     */
    AQRZ("安全认证"),

    /** 
     * 充值
     */
    CZ("充值"),

    /** 
     * 推荐
     */
    TJ("推荐"),

    /** 
     * 投资
     */
    TZ("投资"),

    /** 
     * 注册
     */
    ZC("注册"),

    /** 
     * 投资送用友
     */
    TZSYY("投资送用友");

    protected final String chineseName;

    private T6337_F03(String chineseName){
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
     * @return {@link T6337_F03}
     */
    public static final T6337_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6337_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
