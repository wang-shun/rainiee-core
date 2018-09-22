package com.dimeng.p2p.S63.enums;

import com.dimeng.util.StringHelper;

/**
 * 奖励类型
 * @author heluzhu
 *
 */
public enum T6340_F03 {
	
	/**
	 * 红包
	 */
	redpacket("红包"),
	
	/**
	 * 加息券
	 */
	interest("加息券"),

    /**
     * 体验金
     */
    experience("体验金");
	
	protected final String chineseName;

    private T6340_F03(String chineseName){
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
     * @return {@link T6340_F03}
     */
    public static final T6340_F03 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6340_F03.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
