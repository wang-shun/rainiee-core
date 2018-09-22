package com.dimeng.p2p.S63.enums;

import com.dimeng.util.StringHelper;

/**
 * 使用有效期是否为按月计算
 *
 */
public enum T6344_F09 {

	/**
	 * 按月
	 */
	S("是"),

	/**
	 * 按天
	 */
	F("否");

	protected final String chineseName;

    private T6344_F09(String chineseName){
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
     * @return {@link T6344_F09}
     */
    public static final T6344_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6344_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
