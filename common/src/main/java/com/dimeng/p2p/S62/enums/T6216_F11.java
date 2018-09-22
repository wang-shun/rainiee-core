package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 状态
 */
public enum T6216_F11
{


    
	
	
	 /** 
     * 等额本息
     */
    DEBX("等额本息"),

    /** 
     * 每月付息,到期还本
     */
    MYFX("每月付息,到期还本"),

    /** 
     * 本息到期一次付清
     */
    YCFQ("本息到期一次付清"),

    /** 
     * 等额本金
     */
    DEBJ("等额本金");

    protected final String chineseName;

    private T6216_F11(String chineseName){
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
     * @return {@link T6216_F11}
     */
    public static final T6216_F11 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6216_F11.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
