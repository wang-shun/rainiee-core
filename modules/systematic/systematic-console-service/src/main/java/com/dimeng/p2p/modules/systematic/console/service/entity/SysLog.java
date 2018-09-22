package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.LoginStatus;

/**
 * 系统用户
 * 
 * @author guopeng
 * 
 */
public class SysLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 后台账号表 ID
	 */
	public int id;
	/**
	 * 登录名
	 */
	public String accountName;
	/**
	 * 最后登录时间
	 */
	public Timestamp lastTime;
	/**
	 * 登录IP
	 */
	public String lastIp;
	/**
	 * 是否成功登录(成功;失败)
	 */
	public LoginStatus isSuccess;

}
