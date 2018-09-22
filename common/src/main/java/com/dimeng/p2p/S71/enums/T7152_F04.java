package com.dimeng.p2p.S71.enums;


import com.dimeng.util.StringHelper;

/** 
 * 催收方式
 */
public enum T7152_F04{
	/**
	 * 法律
	 */
    FL("法律"),

    /** 
     * 现场
     */
    XC("现场"),
    
    /** 
     * 电话
     */
    DH("电话"),

    /** 
     * 站内信
     */
    ZNX("站内信"),
    
    /** 
     * 短信
     */
    DX("短信"),
    
    /** 
     * 邮件
     */
    YJ("邮件");

    protected final String chineseName;

    private T7152_F04(String chineseName){
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
     * @return {@link T7152_F04}
     */
    public static final T7152_F04 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T7152_F04.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
