package com.dimeng.p2p.common.enums;

/**
 * 标的状态.
 * 
 */
public enum SignState {
	/**
	 * 申请中
	 */
	SQZ("申请中"),
	/**
	 * 待审核
	 */
	DSH("待审核"),
	/**
	 * 投资中
	 */
	TBZ("投资中"),
	/**
	 * 已满标
	 */
	YMB("已满标"),
	/**
	 * 已放款(还款中)
	 */
	YFK("已放款"),
	/**
	 * 已结清
	 */
	YJQ("已结清"),
	/**
	 * 已垫付
	 */
	YDF("已垫付"),
	/**
	 * 流标
	 */
	LB("流标");

	protected final String name;

	private SignState(String name) {
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
