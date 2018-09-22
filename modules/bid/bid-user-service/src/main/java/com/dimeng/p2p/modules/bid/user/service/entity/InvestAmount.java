package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 累计投资金额实体类
 */
public class InvestAmount {
	
	/**
     * 年
     */
    public int year;
    
    /**
     * 月
     */
    public int month;
    
    /**
     * 投资金额（放款后的金额）
     */
    public BigDecimal investMoney = BigDecimal.ZERO;
    
    /**
     * 买入债权金额
     */
    public BigDecimal buyCreditMoney = BigDecimal.ZERO;

}
