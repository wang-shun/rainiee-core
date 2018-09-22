package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 
 * 是否自动投资
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月15日]
 */
public enum T6250_F09 {
	
	/**
	 * 是否自动投资
	 */
	S("是"),
	
	F("否");
	
	protected final String chineseName;

    private T6250_F09(String chineseName){
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
     * @return {@link T6250_F09}
     */
    public static final T6250_F09 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6250_F09.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }

}
