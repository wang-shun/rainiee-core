package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 是否提前收款
 */
public enum T6056_F14 {

	S("S"), F("F");

	protected final String chineseName;

	private T6056_F14(String chineseName) {
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
	 * @return {@link T6056_F14}
	 */
	public static final T6056_F14 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6056_F14.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
