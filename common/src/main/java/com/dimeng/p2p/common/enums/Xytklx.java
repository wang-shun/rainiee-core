package com.dimeng.p2p.common.enums;

import com.dimeng.util.StringHelper;

/**
 * 协议类型
 */
public enum Xytklx {
	/**
	 * 注册协议
	 */
	ZCXY("注册协议"),

	/**
	 * 个人抵押担保标协议
	 */
	GRDYDBBXY("个人抵押担保标协议"),

	/**
	 * 个人信用标协议
	 */
	GRXYBXY("个人信用标协议"),

	/**
	 * 企业抵押担保标协议
	 */
	QYDYDBBXY("企业抵押担保标协议"),

	/**
	 * 企业信用标协议
	 */
	QYXYBXY("企业信用标协议"),

	/**
	 * 线下债权转让协议
	 */
	XXZQZRXY("线下债权转让协议"),

	/**
	 * 线上债权转让协议
	 */
	XSZQZRXY("线上债权转让协议");

	protected final String chineseName;

	private Xytklx(String chineseName) {
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
	 * @return {@link Xytklx}
	 */
	public static final Xytklx parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		return Xytklx.valueOf(value);
	}
}
