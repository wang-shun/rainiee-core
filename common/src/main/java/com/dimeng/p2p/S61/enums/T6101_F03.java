package com.dimeng.p2p.S61.enums;


import com.dimeng.util.StringHelper;

/** 
 * 账户类型
 */
public enum T6101_F03{


    /** 
     * 往来账户
     */
    WLZH("往来账户"),

    /** 
     * 风险保证金账户
     */
    FXBZJZH("风险保证金账户"),

    /** 
     * 锁定账户
     */
    SDZH("锁定账户");

    protected final String chineseName;

    private T6101_F03(String chineseName){
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
     * @return {@link T6101_F03}
     */
    public static final T6101_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6101_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
