package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 附件类别
 */
public enum T6017_F02 {

	/**
	 * 基本信息认证
	 */
	JBXXRZ("基本信息认证"),

	/**
	 * 公司认证
	 */
	GSRZ("公司认证"),

	/**
	 * 微博认证
	 */
	WBRZ("微博认证"),

	/**
	 * 购车证明
	 */
	GCRZ("购车证明"),

	/**
	 * 房产认证
	 */
	FCRZ("房产认证"),

	/**
	 * 信用报告
	 */
	XYBG("信用报告"),

	/**
	 * 收入证明
	 */
	SRZM("收入证明"),

	/**
	 * 技术职称认证
	 */
	JSZCRZ("技术职称认证"), GZRZ("GZRZ"), JHRZ("JHRZ"),

	/**
	 * 居住地证明
	 */
	JZDZM("居住地证明"),

	/**
	 * 学历认证
	 */
	XLRZ("学历认证"),

	/**
	 * 手机实名认证
	 */
	SJSMRZ("手机实名认证"),

	/**
	 * 身份认证
	 */
	SFRZ("身份认证");

	protected final String chineseName;

	private T6017_F02(String chineseName) {
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
	 * @return {@link T6017_F02}
	 */
	public static final T6017_F02 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6017_F02.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
