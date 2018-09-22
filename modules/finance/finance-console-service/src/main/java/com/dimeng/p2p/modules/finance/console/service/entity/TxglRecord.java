package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.WithdrawStatus;

public class TxglRecord {

	/**
	 * 流水号
	 */
	public int id;
	/**
	 * 用户ID
	 */
	public int userId;

	/**
	 * 用户名
	 */
	public String loginName;

	/**
	 * 姓名
	 */
	public String userName;

	/**
	 * 提现银行
	 */
	public String extractionBank;

	/**
	 * 开户所在地
	 */
	public String location;

	/**
	 * 所在支行
	 */
	public String subbranch;

	/**
	 * 银行卡号
	 */
	public String bankId;

	/**
	 * 提现金额
	 */
	public BigDecimal extractionAmount=new BigDecimal(0);

	/**
	 * 手续费
	 */
	public BigDecimal charge;

	/**
	 * 提现时间
	 */
	public Timestamp applyDateTime;

	/**
	 * 审核时间
	 */
	public Timestamp checkDateTime;
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	/**
	 * 审核人
	 */
	public String approver;
	
	/**
	 * 放款人
	 */
	public String loan;

	/**
	 * 状态(WSH:未审核;SHTG:审核通过;SHSB:审核失败;TXCG:提现成功;TXSB:提现失败)
	 */
	public WithdrawStatus status;

	/**
	 * 备注
	 */
	public String remark;

}
