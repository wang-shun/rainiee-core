package com.dimeng.p2p.common.enums;

import com.dimeng.util.StringHelper;

public enum TermType {
	/**
	 * 注册协议
	 */
	ZCXY("注册协议"),
	/**
	 * 三方借款协议
	 */
	TFJKXY("三方借款协议"),
	/**
	 * 四方借款协议
	 */
	FFJKXY("四方借款协议"),
	/**
	 * 债权转让协议
	 */
	ZQZRXY("债权转让协议"),
	/**
     * 不良债权转让协议
     */
    BLZQZRXY("不良债权转让协议"),
	/**
	 * 公益捐助协议
	 */
	GYJZXY("公益捐助协议"),
	/**
	 * 个人信息采集授权条款
	 */
	GRXXCQSQTK("个人信息采集授权条款"),
	/**
	 * 企业信息采集授权条款
	 */
	QYXXCJSQTK("企业信息采集授权条款"),
	/**
	 * 风险提示函
	 */
	FXTSH("风险提示函");

	protected final String name;

	private TermType(String name) {
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
	 * @return {@link TermType}
	 */
	public static final TermType parse(String value) {
		if(StringHelper.isEmpty(value)){
			return null;
		}
		try{
			return TermType.valueOf(value);
		}catch(Throwable t){
			return null;
		}
	}
}
