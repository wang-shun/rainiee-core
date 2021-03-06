package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 状态
 */
public enum T7029_F05 {

	/**
	 * 有效
	 */
	YX("有效"),

	/**
	 * 无效
	 */
	WX("无效"),

	/**
	 * 删除
	 */
	SC("删除");

	protected final String chineseName;

	private T7029_F05(String chineseName) {
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
	 * @return {@link T7029_F05}
	 */
	public static final T7029_F05 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7029_F05.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
