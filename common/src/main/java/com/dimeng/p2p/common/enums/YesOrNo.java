package com.dimeng.p2p.common.enums;

import com.dimeng.util.StringHelper;

/**
 * 是或者否
 * 
 */
public enum YesOrNo {
	yes("是"), no("否");

	protected final String name;

	private YesOrNo(String name) {
		this.name = name;
	}

	/**
	 * 获取中文名称.
	 * 
	 * @return {@link String}
	 */
	public String getName() {
		return name;
	}

	/**
	 * 解析字符串.
	 *
	 * @return {@link YesOrNo}
	 */
	public static final YesOrNo parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return YesOrNo.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
