package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.sql.Timestamp;


public class Account  {

	
	/** 
	 * 后台账户表id
	 */
	public int accountId;
	/** 
	 * 登录名
	 */
	public String loginName;
	/** 
	 * 密码
	 */
	public String passWord;
	
	/** 
	 * 真实姓名
	 */
	public String userName;
	/** 
	 * 状态(启用,停用)
	 */
	public String status;
	/** 
	 * 创建时间
	 */
	public Timestamp createDateTime;
	/** 
	 * 最后登录时间
	 */
	public Timestamp lastLoginDateTime;
	/** 
	 * 登录IP
	 */
	public String loginIp;

}
