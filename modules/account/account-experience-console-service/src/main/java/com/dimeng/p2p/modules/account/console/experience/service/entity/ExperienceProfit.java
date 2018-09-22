package com.dimeng.p2p.modules.account.console.experience.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6285_F09;

/**
 * 体验金收益详细
 * 
 * @author guopeng
 * 
 */
public class ExperienceProfit implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 标编号
	 */
	public String bidNo;
	/**
	 * 年化利率
	 */
	public BigDecimal rate;
	/**
	 * 借款周期(月)
	 */
	public int num;
	/**
	 * 体验金收益期
	 */
	public int totalNum;
	/**
	 * 还款日
	 */
	public Date time;
	/**
	 * 利息
	 */
	public BigDecimal interest;
	/**
	 * 状态
	 */
	public T6285_F09 status;

	/**
	 * 是否按天借款
	 */
	public T6231_F21 dayOrMonth;

	/**
	 * 借款天数
	 */
	public int borrowDays;

	/**
	 * 体验金投资收益计算方式(true:按月;false:按天)
	 */
	public String incomeMethod;

}
