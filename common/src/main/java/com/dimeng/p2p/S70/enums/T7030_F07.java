package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 类型
 */
public enum T7030_F07 {

	/**
	 * 手动扣除保证金
	 */
	SDKCBZJ("手动扣除保证金"),

	/**
	 * 手动增加保证金
	 */
	SDZJBZJ("手动增加保证金"),

	/**
	 * 借款成交服务费
	 */
	JKCJFWF("借款成交服务费"),

	/**
	 * 垫付返还
	 */
	DFHF("垫付返还"),

	/**
	 * 垫付
	 */
	DF("垫付");

	protected final String chineseName;

	private T7030_F07(String chineseName) {
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
	 * @return {@link T7030_F07}
	 */
	public static final T7030_F07 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7030_F07.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
