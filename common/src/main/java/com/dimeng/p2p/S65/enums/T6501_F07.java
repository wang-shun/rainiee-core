package com.dimeng.p2p.S65.enums;


import com.dimeng.util.StringHelper;

/** 
 * 订单来源
 */
public enum T6501_F07{


    /** 
     * 系统
     */
    XT("系统"),

    /** 
     * 用户
     */
    YH("用户"),

    /** 
     * 后台
     */
    HT("后台"),
    
    /** 
     * poss机
     */
    PS("poss机"),
    
    /** 
     * 委托提现
     */
    WT("委托提现");

    protected final String chineseName;

    private T6501_F07(String chineseName){
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
     * @return {@link T6501_F07}
     */
    public static final T6501_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6501_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
