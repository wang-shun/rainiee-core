package com.dimeng.p2p.modules.bid.front.service.query;

import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.CreditTerm;
import com.dimeng.p2p.common.enums.InvestType;

/**
 * 债权查询接口
 */
public interface TransferAhQuery {
	
	/**
	 * 查询借款标类型.
	 * 
	 * @return {@link InvestType}
	 */
	public abstract T6211[] getType();

	/**
	 * 借款期限
	 * 
	 * @return {@link CreditTerm}
	 */
	public abstract CreditTerm[] getTerm();

	/**
	 * 认证等级
	 * 
	 * @return {@link CreditLevel}
	 */
	public abstract T5124[] getCreditLevel();
	
	/**
	 * 转让金额
	 */
	public abstract int getAmount();
	
	/**
	 * 剩余期限
	 */
	public abstract int getResidue();
	
	/**
	 * 认购收益
	 * @return
	 */
	public abstract int getEarn();
	
	/**
	 * 项目类型
	 * @return
	 */
	public abstract int getTypes();
}
