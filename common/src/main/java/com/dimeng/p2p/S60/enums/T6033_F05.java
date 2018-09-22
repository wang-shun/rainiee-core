package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 状态
 */
public enum T6033_F05 {

	/**
	 * 支付成功
	 */
	ZFCG("支付成功"),

	/**
	 * 未支付
	 */
	WZF("未支付");

	protected final String chineseName;

	private T6033_F05(String chineseName) {
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
	 * @return {@link T6033_F05}
	 */
	public static final T6033_F05 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6033_F05.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
