package com.dimeng.p2p.modules.financing.front.service.entity;

import java.math.BigDecimal;

/**
 * 用户黑名单
 * @author gaoshaolong
 *
 */
public class UserBlack {

	/**
	 * 用户相片路径
	 */
	public String imgPath;
	/**
	 * 用户真实姓名
	 */
	public String realName;
	/**
	 * 登陆名
	 */
	public String loginName;
	/**
	 * 身份证
	 */
	public String card;
	/**
	 * 居住地
	 */
	public String city;
	/**
	 * 手机号码
	 */
	public String telphone;
	/**
	 * 电子邮箱
	 */
	public String email;
	/**
	 * 逾期次数
	 */
	public int yqCount;
	/**
	 * 严重逾期笔数
	 */
	public int yzyqCount;
	/**
	 * 最长逾期天数
	 */
	public int zcyzyqDay;
	/**
	 * 待还金额
	 */
	public BigDecimal dhMoney = new BigDecimal(0);
}
