package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.sql.Timestamp;


public class LoginLog  {

	
	/** 
	 * 后台登录日志id
	 */
	public int logId;
	/** 
	 * 后台账号
	 */
	public int accountId;
	/** 
	 * 登录时间
	 */
	public Timestamp loginDate;
	
	/** 
	 * 登录IP
	 */
	public String loginIP;
	
	/** 
	 * 状态 是否成功登录(成功;失败)
	 */
	public String status;


}
