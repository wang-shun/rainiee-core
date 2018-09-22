package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 收益处理
 */
public enum T6042_F14 {

	/**
	 * 等额本息每月返还
	 */
	DEBXMYHF("等额本息每月返还"),

	/**
	 * 到期本息一次性支付
	 */
	DQBXYCXZF("到期本息一次性支付"),

	/**
	 * 每月还息
	 */
	MYHXDQHB("每月还息");

	protected final String chineseName;

	private T6042_F14(String chineseName) {
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
	 * @return {@link T6042_F14}
	 */
	public static final T6042_F14 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6042_F14.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
