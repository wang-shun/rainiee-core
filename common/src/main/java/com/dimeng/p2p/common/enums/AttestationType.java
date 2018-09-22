package com.dimeng.p2p.common.enums;

/**
 * 认证类型.
 * 
 */
public enum AttestationType {
	/**
	 * 身份认证
	 */
	SFRZ("身份认证"),
	/**
	 * 手机实名认证
	 */
	SJSMRZ("手机实名认证"),
	/**
	 * 学历认证
	 */
	XLRZ("学历认证"),
	/**
	 * 居住地证明
	 */
	JZDZM("居住地证明"),
	/**
	 * 结婚认证
	 */
	JHRZ("结婚认证"),
	/**
	 * 工作认证
	 */
	GZRZ("工作认证"),
	/**
	 * 技术职称认证
	 */
	JSZCRZ("技术职称认证"),
	/**
	 * 收入证明
	 */
	SRZM("收入证明"),
	/**
	 * 信用报告
	 */
	XYBG("信用报告"),
	/**
	 * 房产认证
	 */
	FCRZ("房产认证"),
	/**
	 * 购车证明
	 */
	GCRZ("购车证明"),
	/**
	 * 微博认证
	 */
	WBRZ("微博认证"),
	/**
	 * 公司认证
	 */
	GSRZ("公司认证"),
	/**
	 * 基本信息认证
	 */
	JBXXRZ("基本信息认证");

	protected final String name;

	private AttestationType(String name) {
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
}
