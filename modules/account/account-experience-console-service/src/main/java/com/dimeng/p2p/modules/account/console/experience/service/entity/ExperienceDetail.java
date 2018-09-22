package com.dimeng.p2p.modules.account.console.experience.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6103_F06;

/**
 * 体验金详细信息
 * 
 * @author guopeng
 * 
 */
public class ExperienceDetail implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 用户ID
	 */
	public int userId;
	/**
	 * 标ID
	 */
	public int bidId;
	/**
	 * 用户名
	 */
	public String accountName;
	/**
	 * 姓名
	 */
	public String name;
	/**
	 * 标编号
	 */
	public String bidNo;
	/**
	 * 投资金额
	 */
	public BigDecimal amount;
	/**
	 * 体验金
	 */
	public BigDecimal experience;
	/**
	 * 年化利率
	 */
	public BigDecimal rate;
	/**
	 * 项目期限
	 */
	public int trem;
	/**
	 * 状态
	 */
	public T6103_F06 status;
	/**
	 * 投资时间
	 */
	public Timestamp time;
	/**
	 * 收益
	 */
	public BigDecimal profit;
	/**
	 * 起息日
	 */
	public Date qxTime;
}
