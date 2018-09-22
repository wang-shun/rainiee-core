package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dimeng.p2p.S62.enums.*;

/**
 * 贷款账户
 */
public class LoanAccount implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 贷款标id
	 */
	public int id;
	/**
	 * 借款标题
	 */
	public String title;
	/**
	 * 是否有担保
	 */
	public T6230_F11 F11;
	/**
	 * 是否有抵押
	 */
	public T6230_F13 F13;
	/**
	 * 是否实地认证
	 */
	public T6230_F14 F14;
	/**
	 * 待还本金
	 */
	public BigDecimal dhbj = new BigDecimal(0);
	/**
	 * 待还利息
	 */
	public BigDecimal dhlx = new BigDecimal(0);
	/**
	 * 管理费
	 */
	public BigDecimal glf = new BigDecimal(0);
	/**
	 * 逾期费用
	 */
	public BigDecimal yqfy = new BigDecimal(0);
	/**
	 * 是否新手标
	 */
	public T6230_F28 F15;
	/**
	 * 是否奖励标
	 */
	public T6231_F27 F16;
	/**
	 * 奖励标利率
	 */
	public BigDecimal F17 = new BigDecimal(0);

}
