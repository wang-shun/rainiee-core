package com.dimeng.p2p.S65.enums;

import com.dimeng.util.StringHelper;

/**
 *
 * @author chentongbing
 * @date 2014年10月7日
 */
public enum T6505_F06 {
	
	/**
	 * 是否是友金币订单
	 */
	S("是"),
	
	F("否");
	
	protected final String chineseName;

    private T6505_F06(String chineseName){
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
     * @return {@link T6505_F06}
     */
    public static final T6505_F06 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6505_F06.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }

}
