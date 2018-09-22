package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.RepayStatus;

/**
 * 还款记录列表
 * @author gaoshaolong
 *
 */
public class AlsoMoney implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2078821629327811993L;
	/**
	 * 合约还款日期
	 */
	public Timestamp hyhkrq;
	/**
	 * 状态
	 */
	public RepayStatus status;
	/**
	 * 应付本金
	 */
	public BigDecimal yhbj = new BigDecimal(0);
	/**
	 * 应付利息
	 */
	public BigDecimal yhlx = new BigDecimal(0);
	/**
	 * 应付罚息
	 */
	public BigDecimal yffx= new BigDecimal(0);
	/**
	 * 实际还款日期
	 */
	public Timestamp sjhkTime;
	/**
	 * 应还本息
	 */
	public BigDecimal yhbx =new BigDecimal(0); 
}
