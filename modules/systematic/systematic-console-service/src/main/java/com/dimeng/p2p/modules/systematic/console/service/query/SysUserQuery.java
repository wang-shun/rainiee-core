package com.dimeng.p2p.modules.systematic.console.service.query;

import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SysAccountStatus;

public abstract interface SysUserQuery {
	/**
	 * 用户名称.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getAccountName();

	/**
	 * 用户真实姓名.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract String getName();

	/**
	 * 状态.
	 * 
	 * @return {@link String}空值无效.
	 */
	public abstract SysAccountStatus getStatus();

	/**
	 * 登录时间,大于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getCreateTimeStart();

	/**
	 * 登录时间,小于等于查询.
	 * 
	 * @return {@link Timestamp}null无效.
	 */
	public abstract Timestamp getCreateTimeEnd();

	/**
	 * 角色ID
	 * 
	 * @return
	 */
	public abstract int getRoleId();
	
	/**
	 * 业务员工号
	 * @return
	 */
	public abstract String getEmployNum();

	/**
	 * 所属部门
	 * @return
	 */
	public abstract String getDept();
}
