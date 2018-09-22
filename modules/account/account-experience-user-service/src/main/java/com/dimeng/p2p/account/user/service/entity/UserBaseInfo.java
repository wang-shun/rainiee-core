package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 
 * 用户基础信息
 */
public class UserBaseInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 头像
	 */
	public String photo;
	/**
	 * 账户余额
	 */
	public BigDecimal balance = new BigDecimal(0);
	/**
	 * 冻结资金
	 */
	public BigDecimal freezeFunds = new BigDecimal(0);
	/**
	 * 风险保证金
	 */
	public BigDecimal fxbzj = new BigDecimal(0);
	/**
	 * 安全等级
	 */
	public int safeLevel;
	/**
	 * 手机是否认证
	 */
	public boolean phone = false;
	/**
	 * 是否实名认证
	 */
	public boolean realName = false;
	/**
	 * 是否设置交易密码
	 */
	public boolean withdrawPsw = false;
	/**
	 * 是否绑定邮箱
	 */
	public boolean email = false;
	/**
	 * 体验金
	 */
	public BigDecimal experienceAmount = BigDecimal.ZERO;
	
	/**
	 * 身份证号码
	 */
	public String idCard;

	/**
	 * 实名认证是否企业
	 */
	public boolean auth;
}
