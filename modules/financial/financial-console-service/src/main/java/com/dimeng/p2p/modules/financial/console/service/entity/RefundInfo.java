package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6252_F09;

/**
 * 还款详情
 * @author gongliang
 *
 */
public class RefundInfo {
	
	/**
	 * 合约还款日期
	 */
	public Timestamp contractRefundTime;
	
	/**
	 * 状态
	 */
	public T6252_F09 state;

	/**
	 * 应还本息
	 */
	public BigDecimal  principalInterest = new BigDecimal(0);
	
	/**
	 * 应付罚息
	 */
	public BigDecimal defaultInterest = new BigDecimal(0);

	/**
	 * 实际还款日期
	 */
	public Timestamp practicalRefundTime;

}
