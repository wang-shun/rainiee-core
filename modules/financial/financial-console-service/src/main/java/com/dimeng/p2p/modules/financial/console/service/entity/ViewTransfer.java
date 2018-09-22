package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6230_F10;
import com.dimeng.p2p.S62.enums.T6230_F12;

/**
 * 债权详情信息
 * @author gongliang
 *
 */
public class ViewTransfer {
	/**
	 * 借款标题
	 */
	public String loanRecordTitle;
	
	/**
	 * 帐户名
	 */
	public String userName;

	/**
	 * 剩余期限
	 */
	public int residueDeadline;
	
	/**
	 * 借款期限
	 */
	public int loanDeadline;
	
	/**
	 * 转让价格
	 */
	public BigDecimal transferPrice = new BigDecimal(0);

	/**
	 * 下一还款日
	 */
	public Timestamp refundDay;
	
	/**
	 * 债权价值
	 */
	public BigDecimal residueValue = new BigDecimal(0);
	
	/**
	 * 原始投资金额
	 */
	public BigDecimal tenderMoney = new BigDecimal(0);
	
	/**
	 * 购买总需
	 */
	public BigDecimal gmzx = new BigDecimal(0);
	
	/**
	 * 年化利率
	 */
	public double yearRate;
	
	/**
	 * 还款方式
	 */
	public T6230_F10 repaymentType;
	
	/**
	 * 保障方式
	 */
	public T6230_F12 safeguardWay;
	
	/**
	 * 借款金额
	 */
	public BigDecimal repaymentMoney = new BigDecimal(0);
	
	/**
	 * 借款描述
	 */
	public String jkms;
	
	/**
	 * 债权转让Id
	 */
	public int id;
	
	/**
	 * 借款Id
	 */
	public int jkId;
	
	/**
	 * 用户Id
	 */
	public int userId;
}
