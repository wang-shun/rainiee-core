package com.dimeng.p2p.S62.enums;


import com.dimeng.util.StringHelper;

/** 
 * 选项
 */
public enum T6290_F02{


	 /** 
     * 站内信
     */
	LETTER("站内信"),

    /** 
     * 短信
     */
	SMS("短信"),

    /** 
     * 邮件
     */
	EMAIL("邮件");


    protected final String chineseName;

    private T6290_F02(String chineseName){
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
     * @return {@link T6290_F02}
     */
    public static final T6290_F02 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6290_F02.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
