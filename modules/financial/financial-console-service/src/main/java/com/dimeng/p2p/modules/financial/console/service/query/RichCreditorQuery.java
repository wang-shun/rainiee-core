package com.dimeng.p2p.modules.financial.console.service.query;


public abstract interface RichCreditorQuery extends CreditorQuery {
	
	/**
	 * 借款标题，模糊查询
	 * @return
	 */
	public abstract String getCreditTitle();
}
