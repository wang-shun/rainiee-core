package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.io.Serializable;
/**
 * 在线用户数据
 */
public class UserOnlineData implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 时段
	 */
	public int partTime;
	/**
	 * 在线用户数
	 */
	public int count;

}
