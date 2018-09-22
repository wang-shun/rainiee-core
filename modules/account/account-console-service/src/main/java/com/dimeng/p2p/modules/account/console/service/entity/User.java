package com.dimeng.p2p.modules.account.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;

public class User extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID,自增
	 */
	public int F01;

	/**
	 * 用户登录账号
	 */
	public String F02;

	/**
	 * 手机号码
	 */
	public String F03;

	/**
	 * 邮箱
	 */
	public String F04;

	/**
	 * 用户类型,ZRR:自然人;FZRR:非自然人
	 */
	public T6110_F06 F05;

	/**
	 * 状态,QY启用;SD:锁定;HMD:黑名单;
	 */
	public T6110_F07 F06;

	/**
	 * 注册来源,ZC:注册;HTTJ:后台添加
	 */
	public T6110_F08 F07;

	/**
	 * 注册时间
	 */
	public Timestamp F08;

	/**
	 * 担保方,S:是;F:否;
	 */
	public T6110_F10 F09;
	
	/**
	 * 是否严重逾期
	 */
	public boolean isYzyq;
	/**
	 * 是否已经安全认证
	 */
	public boolean isSafe;
	
	/**
	 * 是否逾期
	 */
	public boolean isYq;
	
	/**
     * 业务员工号
     */
    public String employNum;
	
}