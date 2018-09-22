package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 充值状态
 */
public enum T7049_F05 {

	/**
	 * 已取消
	 */
	YQX("已取消"),

	/**
	 * 已充值
	 */
	YCZ("已充值"),

	/**
	 * 待审核
	 */
	DSH("待审核");

	protected final String chineseName;

	private T7049_F05(String chineseName) {
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
	 * @return {@link T7049_F05}
	 */
	public static final T7049_F05 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7049_F05.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
