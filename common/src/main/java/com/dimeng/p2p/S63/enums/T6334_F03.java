package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * 发放场景
 */
public enum T6334_F03{


    /** 
     * 注册
     */
    ZC("注册"),

    /** 
     * 充值
     */
    CZ("充值"),
    
    TZ("投资"),
    
    RZ("认证");

    protected final String chineseName;

    private T6334_F03(String chineseName){
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
     * @return {@link T6334_F03}
     */
    public static final T6334_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6334_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
