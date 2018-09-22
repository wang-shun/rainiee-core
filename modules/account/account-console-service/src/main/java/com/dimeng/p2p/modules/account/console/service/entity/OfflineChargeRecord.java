package com.dimeng.p2p.modules.account.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S71.entities.T7150;

/**
 * 线下充值记录
 */
public class OfflineChargeRecord extends T7150 {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号名称
	 */
	public String F12;
	/**
	 * 创建用户账号名称
	 */
	public String F13;
	/**
	 * 审核用户账号名称
	 */
	public String F14;
	/**
	 * 充值人姓名
	 */
	public String chargeUserName;
	/**
	 * 充值人类型
	 */
	public String chargeUserType;
	/**
	 * 建立人姓名
	 */
	public String createUserName;
	/**
	 * 审核人姓名
	 */
	public String auditorUserName;
	/**
	 * 审核时间 
	 */
	public Timestamp auditorTime; 
	/**
	 * 用户中心充值人手机号
	 */
	public String telPhone;
}
