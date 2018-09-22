package com.dimeng.p2p.common.enums;

import com.dimeng.util.StringHelper;

/**
 * 活动状态
 * 
 */
public enum BusinessStatus {
	/**
	 * 启用
	 */
	QY("启用"),

	/**
	 * 停用
	 */
	TY("停用");

	protected final String chineseName;

	private BusinessStatus(String chineseName){
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
	 * @return {@link BusinessStatus}
	 */
	public static final BusinessStatus parse(String value) {
		if(StringHelper.isEmpty(value)){
			return null;
		}
		try{
			return BusinessStatus.valueOf(value);
		}catch(Throwable t){
			return null;
		}
	}
}
