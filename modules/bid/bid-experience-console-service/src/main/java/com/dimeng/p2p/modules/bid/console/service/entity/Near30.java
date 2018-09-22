package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;
import java.sql.Date;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;

/**
 * 催收近30天待还信息
 * 
 * @author gongliang
 * 
 */
public class Near30 {
	/**
	 * 催收Id
	 * 
	 */
	public int collectionId;

	/**
	 * 借款Id
	 * 
	 */
	public int loanRecordId;

	/**
	 * 帐户Id
	 */
	public int userId;

	/**
	 * 借款标题
	 * 
	 */
	public String loanRecordTitle;

	/**
	 * 借款标类型
	 */
	public String signType;

	/**
	 * 帐户名
	 */
	public String userName;

	/**
	 * 借款金额
	 */
	public BigDecimal loanAmount = new BigDecimal(0);

	/**
	 * 借款期数
	 */
	public String loandeadline;

	/**
	 * 本期应还金额（元）
	 */
	public BigDecimal principalAmount = new BigDecimal(0);

	/**
	 * 剩余应还总额（元）
	 */
	public BigDecimal residueAmount = new BigDecimal(0);

	/**
	 * 距离下次还款
	 */
	public int distanceRefund;

	/**
	 * 下一还款日
	 */
	public Date refundDay;
	
	/**
	 * 用户类型
	 */
	public T6110_F06 userType;
	/**
	 * 担保方
	 */
	public T6110_F10 dbf;
	
	/**
	 * 手机号码
	 */
	public String mobile;
	
	/**
	 * 邮箱地址
	 */
	public String email;
}
