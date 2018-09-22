/*
 * 文 件 名:  CheckBalanceTotalAmount.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月23日
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;

/**
 * 平台累计调账收入，累计调账支出
 * @author  xiaoqi
 * @version  [版本号, 2015年11月23日]
 */
public class CheckBalanceTotalAmount {

	/**
	 * 累计调账收入
	 */
	public BigDecimal totalIncomeAmount = BigDecimal.ZERO;
	 /**
	  * 累计调账支出
	  */
	public BigDecimal totalExpendAmount = BigDecimal.ZERO;
}
