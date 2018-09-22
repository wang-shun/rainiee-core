package com.dimeng.p2p.common.enums;

/**
 * 计划状态.
 * 
 */
public enum PlanState {
	/**
	 * 新建
	 */
	XJ("新建"),
	/**
	 * 已发布
	 */
	YFB("已发布"),
	/**
	 * 已生效
	 */
	YSX("已生效"),
	/**
	 * 已截止
	 */
	YJZ("已截止");

	protected final String name;

	private PlanState(String name) {
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
