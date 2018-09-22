package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;

/**
 * 债权信息
 * 
 * @author gongliang
 * 
 */
public class Creditor {
	/**
	 * 天数
	 */
	public int day;
	/**
	 * 是否按天计算
	 */
	public T6231_F21 f21;
	/**
	 * 债权ID
	 */
	public String creditorId;

	/**
	 * 卖出者
	 */
	public String userName;
	/**
	 * 买入者
	 */
	public String mrzName;

	/**
	 * 年化利率
	 */
	public double yearRate;

	/**
	 * 期限
	 */
	public int deadline;

	/**
	 * 原始投资金额
	 */
	public BigDecimal investmentAmount = new BigDecimal(0);

	/**
	 * 投资时间
	 */
	public Timestamp tenderTime;

	/**
	 * 状态
	 */
	public T6230_F20 creditorState;

	/**
	 * 借款Id
	 */
	public int jkId;
	/**
	 * 借款标题
	 */
	public String jkbt;

	/**
	 * 购买价格
	 */
	public BigDecimal gmjg = new BigDecimal(0);

	/**
	 * 受让价格
	 */
	public BigDecimal srjg = new BigDecimal(0);
    
    /**
     * 债权价值
     */
    public BigDecimal creditorValue = new BigDecimal(0);
    
    /**
     * 转让价值
     */
    public BigDecimal transferValue = new BigDecimal(0);

    /**
     * 投资人类型（个人、机构、企业）
     */
    public String investUserType;
    
    /**
     * 债权人类型（个人、机构、企业）
     */
    public String creditorType;
    
    /**
     * 卖出者类型（个人、机构、企业）
     */
    public String sellUserType;
    
    /**
     * 买入者类型（个人、机构、企业）
     */
    public String buyUserType;
}
