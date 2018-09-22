package com.dimeng.p2p.modules.financing.user.service.query;

import java.math.BigDecimal;

public interface addTransfer {

	/**
	 * 债权转让ID
	 * 
	 * @return {@link int}
	 */
	public abstract int getTransferId();
	/**
	 * 
	 * 借款标ID
	 * @return {@link int}
	 */
	public abstract int getLoanId();
	/**
	 * 标的价值
	 * @return
	 */
	public abstract BigDecimal getBidValue();
	/**
	 * 转出价格
	 * @return
	 */
	public abstract BigDecimal getTransferValue();
	/**
	 * 转出份数
	 * @return
	 */
	public abstract int getOverNum();
	
	/**
	 * 预计收益
	 * @return
	 */
	public abstract BigDecimal getMayMoney();
}
