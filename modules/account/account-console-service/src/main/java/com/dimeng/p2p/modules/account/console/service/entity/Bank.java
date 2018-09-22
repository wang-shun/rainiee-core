/*
 * 文 件 名:  Bank.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guopeng
 * 修改时间:  2014年7月7日
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.io.Serializable;
/**
 * 
 * 银行实体类
 * 
 * @author  guopeng
 * @version  [版本号, 2014年7月7日]
 */
public class Bank implements Serializable {
	
	private static final long serialVersionUID = 1L;

	/**
	 * 主键ID
	 */
	public int id;
	/**
	 * 银行名称
	 */
	public String name;

	/**
	 * 银行简称
	 */
	public String code;
	
	/**
	 * 状态  TY-停用  QY-启用
	 */
	public String status;
}
