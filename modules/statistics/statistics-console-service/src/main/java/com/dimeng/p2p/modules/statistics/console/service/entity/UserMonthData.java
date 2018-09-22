package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
/**
 * 按月统计用户数据
 */
public class UserMonthData implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 月份
	 */
	public int month;
	/**
	 * 平台用户数
	 */
	public int userCount;
	/**
	 * 新增用户数
	 */
	public int newUserCount;
	/**
	 * 借款用户数
	 */
	public int jkUserCount;
	/**
	 * 投资用户数
	 */
	public int tbUserCount;
	/**
	 * 新增借款用户数
	 */
	public int jkNewUserCount;
	/**
	 * 新增投资用户数
	 */
	public int tbNewUserCount;
}
