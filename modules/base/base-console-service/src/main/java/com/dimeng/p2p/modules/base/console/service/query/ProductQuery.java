package com.dimeng.p2p.modules.base.console.service.query;


import com.dimeng.p2p.S62.enums.T6216_F04;

/**
 * 产品信息
 * 
 */
public abstract interface ProductQuery
{
	/**
	 * 产品名称
	 * 
	 * @return
	 */
	public abstract String getProductName();

	/**
	 * 产品状态, QY:启用;TY:停用
	 * 
	 * @return
	 */
	public abstract T6216_F04 getStatus();
	
	/**
	 * 标类型
	 * 
	 * @return
	 */
	public abstract String getBidType();
	
	/**
	 * 还款方式
	 * 
	 * @return
	 */
	public abstract String getRepaymentWay();
}
