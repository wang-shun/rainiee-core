package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 消息类型
 */
public enum T7014_F02 {

	/**
	 * 站内信
	 */
	ZNX("站内信"),

	/**
	 * 邮件
	 */
	YJ("邮件"),

	/**
	 * 短信
	 */
	DX("短信");

	protected final String chineseName;

	private T7014_F02(String chineseName) {
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
	 * @return {@link T7014_F02}
	 */
	public static final T7014_F02 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7014_F02.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
