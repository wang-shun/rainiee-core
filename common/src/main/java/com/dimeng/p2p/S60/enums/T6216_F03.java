package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 附件类型
 */
public enum T6216_F03 {

	QT("QT"), XGZZ("XGZZ"), FC("FC"), XY("XY");

	protected final String chineseName;

	private T6216_F03(String chineseName) {
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
	 * @return {@link T6216_F03}
	 */
	public static final T6216_F03 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6216_F03.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
