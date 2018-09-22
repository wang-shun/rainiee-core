package com.dimeng.p2p.S71.enums;


import com.dimeng.util.StringHelper;

/** 
 * 是否是后台线下充值,否：用户中心充值
 */
public enum T7150_F12{


    /** 
     * 往来账户
     */
    S("后台充值"),

    /** 
     * 风险保证金账户
     */
    F("用户中心充值");

    protected final String chineseName;

    private T7150_F12(String chineseName){
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
     * @return {@link T7150_F12}
     */
    public static final T7150_F12 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T7150_F12.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
