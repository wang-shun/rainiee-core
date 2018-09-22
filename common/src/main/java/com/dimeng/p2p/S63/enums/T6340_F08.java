package com.dimeng.p2p.S63.enums;

import com.dimeng.util.StringHelper;

/**
 * 活动状态
 * @author heluzhu
 *
 */
public enum T6340_F08 {
	
	/**
	 * 待上架
	 */
	DSJ("待上架"),
	
	/**
	 * 预上架
	 */
	YSJ("预上架"),
	
	/**
	 * 进行中
	 */
	JXZ("进行中"),
	
	/**
	 * 已下架
	 */
	YXJ("已下架"),
	
	/**
	 * 已作废
	 */
	YZF("已作废");
	
	protected final String chineseName;

    private T6340_F08(String chineseName){
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
     * @return {@link T6340_F08}
     */
    public static final T6340_F08 parse(String value) {
        if(StringHelper.isEmpty(value)){
            return null;
        }
        try{
            return T6340_F08.valueOf(value);
        }catch(Throwable t){
            return null;
        }
    }
}
