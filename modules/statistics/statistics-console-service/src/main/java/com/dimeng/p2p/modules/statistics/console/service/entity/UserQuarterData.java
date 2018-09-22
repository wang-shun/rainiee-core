package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;

/**
 *  按季度统计用户数据
 */
public class UserQuarterData implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 季度
	 */
	public int quarter;
	/**
	 * 平台用户数
	 */
	public int userCount;
	/**
	 * 新增用户数
	 */
	public int newUserCount;
	/**
	 * 平台法人
	 */
	public int jkUserCount;
	/**
	 * 平台自然人
	 */
	public int tbUserCount;
	/**
	 * 新增平台法人
	 */
	public int jkNewUserCount;
	/**
	 * 新增平台自然人
	 */
	public int tbNewUserCount;
}
