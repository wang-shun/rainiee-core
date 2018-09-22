package com.dimeng.p2p.modules.bid.front.service.query;

import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.common.enums.InvestType;

/**
 * 债权查询接口
 */
public interface TransferQuery {
	
	/**
	 * 查询借款标类型.
	 * 
	 * @return {@link InvestType}
	 */
	public abstract T6211[] getType();
	
	/**
     * 年化利率
     * 
     * @return
     */
    public abstract int getRate();

	/**
	 * 借款期限
	 * 
	 * @return {@link CreditTerm}
	 */
    /*public abstract CreditTerm[] getTerm();*/
    public abstract int getTerm();

	/**
	 * 认证等级
	 * 
	 * @return {@link CreditLevel}
	 */
	public abstract T5124[] getCreditLevel();
	
	public abstract int getProductId();
}
