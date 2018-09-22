/**
 * 
 */
package com.dimeng.p2p.S62.enums;

import com.dimeng.util.StringHelper;

/**
 * 投资奖励发放记录状态
 * @author guomianyun
 *
 */
public enum T6287_F07 {

	 /** 
     * 否
     */
	DFF("待发放"),

    /** 
     * 是
     */
	YFF("已发放"),
	
	/**
	 * 未发
	 */
	BFF("不发放");

    protected final String chineseName;

    private T6287_F07(String chineseName){
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
     * @return {@link T6286_F08}
     */
    public static final T6287_F07 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6287_F07.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
