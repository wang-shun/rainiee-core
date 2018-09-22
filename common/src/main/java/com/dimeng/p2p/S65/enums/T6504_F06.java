package com.dimeng.p2p.S65.enums;

import com.dimeng.util.StringHelper;

/**
 * 
 * 是否自动投资订单
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月15日]
 */
public enum T6504_F06 {
	
	/**
	 * 是否自动投资订单
	 */
	S("是"),
	
	F("否");
	
	protected final String chineseName;

    private T6504_F06(String chineseName){
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
     * @return {@link T6504_F06}
     */
    public static final T6504_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6504_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }

}
