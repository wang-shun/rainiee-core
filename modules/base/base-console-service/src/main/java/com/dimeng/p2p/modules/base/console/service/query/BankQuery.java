package com.dimeng.p2p.modules.base.console.service.query;

import com.dimeng.p2p.S50.enums.T5020_F03;

/**
 * 银行信息
 * 
 */
public abstract interface BankQuery {
	/**
	 * 银行名称
	 * 
	 * @return
	 */
	public abstract String getBankName();

	/**
	 * 银行状态, QY:启用;TY:停用
	 * 
	 * @return
	 */
	public abstract T5020_F03 getStatus();

}
