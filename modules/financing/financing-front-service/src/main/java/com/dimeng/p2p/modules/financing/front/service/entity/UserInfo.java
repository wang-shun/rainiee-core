package com.dimeng.p2p.modules.financing.front.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.CreditLevel;

public class UserInfo {
	/**
	 * 登陆Id
	 */
	public int loginId;
	/**
	 * 登陆名
	 */
	public String loginName;
	/**
	 * 可用余额
	 */
	public BigDecimal kyMoney = new BigDecimal(0);
	/**
	 * 年龄
	 */
	public int age;
	/**
	 * 性别
	 */
	public int sex;
	/**
	 * 注册时间
	 */
	public Timestamp zcsj;
	/**
	 * 信用等级
	 */
	public CreditLevel creditLevel;
	
	/**
	 * 用户相片路径
	 */
	public String imgPath;
	/**
	 * 持有债权数量
	 */
	public int cyzqsl;
	/**
	 * 持有理财计划数量
	 */
	public int cylcjhsl;
	/**
	 * 发布借款数量
	 */
	public int fbjksl;
	/**
	 * 成功借款数量
	 */
	public int cgjksl;
	/**
	 * 未还清借款数量
	 */
	public int whqjksl;
	/**
	 * 逾期次数
	 */
	public int yqcs;
	/**
	 * 逾期金额
	 */
	public BigDecimal yqje=new BigDecimal(0);
	/**
	 * 严重逾期次数
	 */
	public int yzyqcs;

}
