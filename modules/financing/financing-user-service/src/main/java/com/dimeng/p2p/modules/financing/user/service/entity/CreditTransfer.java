package com.dimeng.p2p.modules.financing.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.IssueState;
import com.dimeng.p2p.common.enums.TransferState;

/**
 * 债权转让信息
 * 
 */
public class CreditTransfer implements Serializable {

	private static final long serialVersionUID = 4000187865062883911L;

	/**
	 * 借款标ID
	 */
	public int jkbId;
	/**
	 * 转出id
	 */
	public int zqzcId;
	/**
	 * 转让价格
	 */
	public BigDecimal transerMoney = new BigDecimal(0);
	/**
	 * 债权价格
	 */
	public BigDecimal creditMoney = new BigDecimal(0);
	/**
	 * 剩余份数
	 */
	public int overCount;

	/**
	 * 债权转出用户id
	 */
	public int sellUserId;
	/**
	 * 债权ID
	 */
	public int zqId;
	/**
	 * 是否发布状态
	 */
	public IssueState issueState;
	/**
	 * 转让状态
	 */
	public TransferState transferState;
}
