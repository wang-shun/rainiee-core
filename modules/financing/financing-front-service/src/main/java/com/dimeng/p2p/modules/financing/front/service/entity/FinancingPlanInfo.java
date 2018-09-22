package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.EarningsWay;
import com.dimeng.p2p.common.enums.EnsureMode;
import com.dimeng.p2p.common.enums.PlanState;
import com.dimeng.p2p.common.enums.SignType;

/**
 * 优选理财详情
 *
 */
public class FinancingPlanInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2959245138389692271L;
	
	/**
	 * 计划ID
	 */
	public int id;
	/**
	 * 标题
	 */
	public String planTitle;
	/**
	 * 计划金额
	 */
	public BigDecimal planMoney = new BigDecimal(0);
	/**
	 * 实际投资金额
	 */
	public BigDecimal ActualMoney = new BigDecimal(0);
	/**
	 * 预期收益
	 */
	public BigDecimal yqsy = new BigDecimal(0);
	/**
	 * 保障方式
	 */
	public EnsureMode ensureMode;
	/**
	 * 计划状态
	 */
	public PlanState planState;
	/**
	 * 满额用时（秒计算）
	 */
	public int fullTime;
	/**
	 * 距离截止
	 */
	public Timestamp cutoff;
	/**
	 * 距离发售
	 */
	public Timestamp fromSale;
	/**
	 * 锁定结束时间
	 */
	public Timestamp lockEnd;
	/**
	 * 锁定期限
	 */
	public int lockqx;
	/**
	 * 收益处理
	 */
	public EarningsWay earningsWay;
	/**
	 * 加入费率
	 */
	public double jrfl;
	/**
	 * 服务费率
	 */
	public double fwfl;
	/**
	 * 退出费率
	 */
	public double tcfl;
	
	/**
	 * 剩余金额
	 */
	public BigDecimal syMoney = new BigDecimal(0);
	/**
	 * 最多投资金额
	 */
	public BigDecimal maxMoney = new BigDecimal(0);
	/**
	 * 至少投资金额
	 */
	public BigDecimal minMoney = new BigDecimal(0);
	/**
	 * 累计总金额
	 */
	public BigDecimal totleMoney = new BigDecimal(0);
	/**
	 * 加入总人次
	 */
	public long joinTotle; 
	/**
	 * 资金利用率
	 */
	public BigDecimal moneyUtilization = new BigDecimal(0);
	/**
	 * 为用户总共赚取
	 */
	public BigDecimal totleEarn = new BigDecimal(0);
	/**
	 * 投资范围
	 */
	public SignType signType;
	
	/**
	 * 进度
	 */
	public double proess;
	
	/**
	 * 计划介绍
	 */
	public String introduce;
	/**
	 * 数据库系统时间
	 */
	public Timestamp currentTime;
	/**
	 * 申请结束日期
	 */
	public Timestamp sqjsrq;
}
