package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 线上债权转让协议
 * @author gaoshaolong
 *
 */
public class Xszqjkxy {
	/**
	 * 借款ID
	 */
	public int loanId;
	/**
	 * 转让申请ID
	 */
	public int zrsqId;
	/**
	 * 协议编号
	 */
	public String xyNo;
	/**
	 * 转让者
	 */
	public String zrName;
	/**
	 * 受让者
	 */
	public String srName;
	/**
	 * 转让总价值
	 */
	public BigDecimal zrzjz = new BigDecimal(0);
	/**
	 * 转让价格
	 */
	public BigDecimal zrjg = new BigDecimal(0);
	/**
	 * 转让时间
	 */
	public Timestamp time;
}
