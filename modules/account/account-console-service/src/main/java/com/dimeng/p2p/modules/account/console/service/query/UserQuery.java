package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;


/**
 * 用户列表查询
 *
 */
public class UserQuery extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 登录账号
	 */
	public String userName;
	/**
	 * 手机号
	 */
	public String phone;
	/**
	 * 邮箱
	 */
	public String eamil;
	/**
	 * 状态  启用QY;锁定SD
	 */
	public T6110_F07 status;
	/**
	 * 注册来源  注册ZC；后台添加HTTJ
	 */
	public T6110_F08 zcly;
	/**
	 * 注册开始时间
	 */
	public Timestamp startTime;
	/**
	 * 注册结束时间
	 */
	public Timestamp endTime;
	/**
	 * 账号类型  GR个人;QY企业;JG机构
	 */
	public String zhlx;
	/**
	 * 业务员工号
	 */
	public String employNum;
	
}
