package com.dimeng.p2p.modules.base.console.service.query;

import com.dimeng.p2p.S51.enums.T5124_F05;

/**
 * 信用等级
 * 
 */
public abstract interface CreditLevelQuery {
	/**
	 * 等级名称
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * 状态
	 */
	public abstract T5124_F05 getStatus();

}
