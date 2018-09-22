/*
 * 文 件 名:  T6196.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月10日
 */
package com.dimeng.p2p.S61.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <运营数据基础设置> <功能详细描述>
 * 
 * @author zhoucl
 * @version [版本号, 2016年3月10日]
 */
public class T6196 extends AbstractEntity {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 6065086876434332724L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 累计投资金额
	 */
	public BigDecimal F02 = BigDecimal.ZERO;

	/**
	 * 注册用户数
	 */
	public int F03;

	/**
	 * 累计赚取收益
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 累计成交笔数
	 */
	public int F05;

	/**
	 * 投资用户数
	 */
	public int F06;

	/**
	 * 借款用户数
	 */
	public int F07;

	/**
	 * 90后
	 */
	public int F08;

	/**
	 * 80后
	 */
	public int F09;

	/**
	 * 70后
	 */
	public int F10;

	/**
	 * 60后
	 */
	public int F11;

	/**
	 * 其他
	 */
	public int F12;

	/**
	 * 0-3个月
	 */
	public BigDecimal F13 = BigDecimal.ZERO;

	/**
	 * 3-6个月
	 */
	public BigDecimal F14 = BigDecimal.ZERO;

	/**
	 * 6-9个月
	 */
	public BigDecimal F15 = BigDecimal.ZERO;

	/**
	 * 9-12个月
	 */
	public BigDecimal F16 = BigDecimal.ZERO;

	/**
	 * 12-24个月
	 */
	public BigDecimal F17 = BigDecimal.ZERO;

	/**
	 * 24个月以上
	 */
	public BigDecimal F18 = BigDecimal.ZERO;

	/**
	 * 担保标
	 */
	public BigDecimal F19 = BigDecimal.ZERO;

	/**
	 * 抵押认证标
	 */
	public BigDecimal F20 = BigDecimal.ZERO;

	/**
	 * 实地认证标
	 */
	public BigDecimal F21 = BigDecimal.ZERO;

	/**
	 * 信用认证标
	 */
	public BigDecimal F22 = BigDecimal.ZERO;

	/**
	 * 累计代偿金额
	 */
	public BigDecimal F23 = BigDecimal.ZERO;

	/**
	 * 最大单户借款余额占比
	 */
	public BigDecimal F24 = BigDecimal.ZERO;

	/**
	 * 最大10户借款余额占比
	 */
	public BigDecimal F25 = BigDecimal.ZERO;

	/**
	 * 借款逾期金额
	 */
	public BigDecimal F26 = BigDecimal.ZERO;

	/**
	 * 借款逾期率
	 */
	public BigDecimal F27 = BigDecimal.ZERO;

	/**
	 * 借款坏账率
	 */
	public BigDecimal F28 = BigDecimal.ZERO;

	/**
	 * 最后操作时间
	 */
	public Timestamp F29;

	/**
	 * 其他(天标)
	 */
	public BigDecimal F30 = BigDecimal.ZERO;

	/**
	 * 借贷余额
	 */
	public BigDecimal F31 = BigDecimal.ZERO;

	/**
	 * 借贷余额笔数
	 */
	public int F32;

	/**
	 * 利息余额
	 */
	public BigDecimal F33 = BigDecimal.ZERO;

	/**
	 * 累计出借人数量
	 */
	public int F34;

	/**
	 * 当前出借人数量
	 */
	public int F35;

	/**
	 * 累计借款人数量
	 */
	public int F36;

	/**
	 * 当前借款人数量
	 */
	public int F37;

	/**
	 * 最大单一借款人待还金额占比
	 */
	public BigDecimal F38 = BigDecimal.ZERO;

	/**
	 * 前十大借款人待还金额占比
	 */
	public BigDecimal F39 = BigDecimal.ZERO;

	/**
	 * 关联关系借款金额
	 */
	public BigDecimal F40 = BigDecimal.ZERO;

	/**
	 * 关联关系借款笔数
	 */
	public int F41;

	/**
	 * 逾期笔数
	 */
	public int F42;

	/**
	 * 项目逾期率
	 */
	public BigDecimal F43 = BigDecimal.ZERO;

	/**
	 * 逾期90天（不含）以上金额
	 */
	public BigDecimal F44 = BigDecimal.ZERO;

	/**
	 * 逾期90天（不含）以上笔数
	 */
	public int F45;

	/**
	 * 累计代偿笔数
	 */
	public int F46;

}
