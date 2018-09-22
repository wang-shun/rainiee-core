/*
 * 文 件 名:  T7052
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/6/7
 */
package com.dimeng.p2p.S70.entities;

import com.dimeng.framework.service.AbstractEntity;

import java.sql.Timestamp;

/**
 * 平台用户数统计
 * <功能详细描述>
 *
 * @author heluzhu
 * @version [版本号, 2016/6/7]
 */
public class T7052 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 日期
	 */
	public String F01;
	 
	/**
	 * 注册用户数
	 */
	public int F02;

	/**
	 * PC端注册用户数
	 */
	public int F03;

	/**
	 * APP端注册用户数
	 */
	public int F04;

	/**
	 * 微信端注册用户数
	 */
	public int F05;

	/**
	 * 后台注册用户数
	 */
	public int F06;

	/**
	 * 登录用户数
	 */
	public int F07;

	/**
	 * 充值用户数
	 */
	public int F08;

	/**
	 * 提现用户数
	 */
	public int F09;

	/**
	 * 投资用户数
	 */
	public int F10;

	/**
	 * 借款用户数
	 */
	public int F11;

	/**
	 * 更新时间
	 */
	public Timestamp F12;

	/**
	 * 年份
	 */
	public String F13;

	/**
	 * 季度（年份+季度）
	 */
	public String F14;

	/**
	 * 月份（年份+月份）
	 */
	public String F15;

	/**
	 * 年份
	 */
	public String F16;
}
