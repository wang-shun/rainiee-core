/*
 * 文 件 名:  AddZhList.java
 * 版    权:  © 2014 DM. All rights reserved.
 * 描    述:  体验金充值-新增选择充值人员-查询条件
 * 修 改 人:  linxiaolin
 * 修改时间:  2015/3/19
 * 跟踪单号:  <跟踪单号>
 * 修改单号:  <修改单号>
 * 修改内容:  <修改内容>
 */
package com.dimeng.p2p.modules.account.console.experience.service.query;

import com.dimeng.framework.service.AbstractEntity;


/**
 * 体验金充值-新增选择充值人员-查询条件
 * @author linxiaolin
 * @version [1.0, 2015/3/19]
 * @see [相关类/方法]
 * @since [产品/模块版本]
 */
public class AddExperienceUserQuery extends AbstractEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * 登录账号
	 */
	public String userName;
	/**
	 * 姓名
	 */
	public String realName;

}
