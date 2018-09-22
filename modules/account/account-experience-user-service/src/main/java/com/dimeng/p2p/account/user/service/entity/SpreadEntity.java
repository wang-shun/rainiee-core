package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class SpreadEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6135237981466703057L;
	
	/**
	 * 被推广的用户名
	 */
	public String userName;
	
	/**
	 * 注册时间
	 */
	public Timestamp zcTime;
}
