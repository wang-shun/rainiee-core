package com.dimeng.p2p.S63.enums;

import com.dimeng.util.StringHelper;

/**
 * 生日赠送领取条件
 * @author heluzhu
 *
 */
public enum T6340_F09 {
	
	/**
	 * 生日当天登录
	 */
	login("生日当天登录"),
	
	/**
	 * 生日当天投资
	 */
	invest("生日当天投资"),
	
	/**
	 * 不限
	 */
	all("不限");
	
	protected final String chineseName;

    private T6340_F09(String chineseName){
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
     * @return {@link T6340_F09}
     */
    public static final T6340_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6340_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
