package com.dimeng.p2p.modules.financial.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6260_F07;

/**
 * 已转出债权
 * @author gongliang
 *
 */
public class TransferFinish {
	
	/**
	 * 转让ID
	 */
	public int transferId;
	
	/**
	 * 债权ID
	 */
	public int creditorId;
	
	/**
	 * 债权卖出者
	 */
	public String saleUserName;

	/**
	 * 期限
	 */
	public int deadline;
	
	/**
	 * 剩余期限
	 */
	public int residueDeadline;
	
	/**
	 * 年化利率
	 */
	public double yearRate;

	/**
	 * 债权价值
	 */
	public BigDecimal creditorValue = new BigDecimal(0);
	
	/**
	 * 转让价格
	 */
	public BigDecimal transferPrice = new BigDecimal(0);
	
	/**
	 * 状态
	 */
	public T6260_F07 creditorTransferState;
	/**
	 * 借款标题
	 */
	public String title;
	
	/**
	 * 审核时的Id
	 */
	public int tempId;
	
	/**
	 * 借款Id
	 */
	public int jkId;
	
	/**
	 * 用户Id
	 */
	public int userId;
	
	/**
	 * 转出时间
	 */
	public Timestamp outTime;
	
}
