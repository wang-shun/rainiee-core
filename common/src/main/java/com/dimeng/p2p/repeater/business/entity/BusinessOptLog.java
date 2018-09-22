package com.dimeng.p2p.repeater.business.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * 业务员锁定日志
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
public class BusinessOptLog implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 逐渐
	 */
	public int id;
	/**
	 * 业务员工号
	 */
	public String employNum;
	/**
	 * 锁定时间
	 */
	public Timestamp lockTime;
	/**
	 * 解锁时间
	 */
	public Timestamp unLockTime;
	/**
	 * 状态
	 */
	public String status;

}
