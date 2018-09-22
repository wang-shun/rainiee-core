package com.dimeng.p2p.modules.bid.user.service.query;

import java.math.BigDecimal;

public interface addTransfer {

	/**
	 * 债权ID
	 * 
	 * @return {@link int}
	 */
	public abstract int getTransferId();
	/**
	 * 转让债权
	 * @return
	 */
	public abstract BigDecimal getBidValue();
	/**
	 * 转出价格
	 * @return
	 */
	public abstract BigDecimal getTransferValue();
	/**
	 * 转让手续费
	 * @return
	 */
	public abstract BigDecimal getRateMoney();
	
}
