package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 发送对象
 */
public enum T7017_F06 {

	/**
	 * 所有
	 */
	SY("所有"),

	/**
	 * 指定人
	 */
	ZDR("指定人");

	protected final String chineseName;

	private T7017_F06(String chineseName) {
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
	 * @return {@link T7017_F06}
	 */
	public static final T7017_F06 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7017_F06.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
