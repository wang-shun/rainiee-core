package com.dimeng.p2p.S61.enums;

import com.dimeng.util.StringHelper;
/** 
 * 是否授权
 */
public enum T6171_F03 {
	


	    /** 
	     * 是
	     */
	    Y("是"),

	    /** 
	     * 否
	     */
	    N("否");

	    protected final String chineseName;

	    private T6171_F03(String chineseName){
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
	     * @return {@link T6230_F11}
	     */
	    public static final T6171_F03 parse(String value) {
	        if(StringHelper.isEmpty(value)){
	            return null;
	        }
	        try{
	            return T6171_F03.valueOf(value);
	        }catch(Throwable t){
	            return null;
	        }
	    }

}
