/*
 * 文 件 名:  User.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  体验金充值-新增选择充值人员
 * 修 改 人:  linxiaolin
 * 修改时间:  2015/3/19
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.modules.account.console.experience.service.entity;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F07;

/**
 * 体验金充值-新增选择充值人员
 * @author linxiaolin
 * @version [1.0, 2015/3/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
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
	 * 姓名
	 */
	public String F03;

	/**
	 * 状态,QY启用;SD:锁定;HMD:黑名单;
	 */
	public T6110_F07 F04;


	
}