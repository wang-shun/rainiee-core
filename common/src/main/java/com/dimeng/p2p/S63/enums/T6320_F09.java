package com.dimeng.p2p.S63.enums;


import com.dimeng.util.StringHelper;

/** 
 * 优惠券获取方式
 */
public enum T6320_F09{


    /** 
     * 直接发放
     */
    ZJFF("直接发放"),

    /** 
     * 用户领取
     */
    YHLQ("用户领取"),

    /** 
     * 推广
     */
    TG("推广"),

    /** 
     * 充值
     */
    CZ("充值");

    protected final String chineseName;

    private T6320_F09(String chineseName){
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
     * @return {@link T6320_F09}
     */
    public static final T6320_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6320_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
