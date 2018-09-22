package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 
 * <运营数据统计> <功能详细描述>
 * 
 * @author liulixia
 * @version [版本号, 2018年2月7日]
 */
public class T7054 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 累计交易金额
	 */
	public BigDecimal F01 = BigDecimal.ZERO;

	/**
	 * 累计交易笔数
	 */
	public int F02;

	/**
	 * 借贷余额
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 借贷余额笔数
	 */
	public int F04;

	/**
	 * 累计出借人数量
	 */
	public int F05;

	/**
	 * 当前出借人数量
	 */
	public int F06;

	/**
	 * 累计借款人数量
	 */
	public int F07;

	/**
	 * 当前借款人数量
	 */
	public int F08;

	/**
	 * 最大单一借款人待还金额占比
	 */
	public BigDecimal F09 = BigDecimal.ZERO;

	/**
	 * 前十大借款人待还金额占比
	 */
	public BigDecimal F10 = BigDecimal.ZERO;

	/**
	 * 注册用户数
	 */
	public int F11;

	/**
	 * 利息余额
	 */
	public BigDecimal F12 = BigDecimal.ZERO;

	/**
	 * 累计赚取收益
	 */
	public BigDecimal F13 = BigDecimal.ZERO;

	/**
	 * 关联关系借款金额
	 */
	public BigDecimal F14 = BigDecimal.ZERO;

	/**
	 * 关联关系借款笔数
	 */
	public int F15;

	/**
	 * 逾期金额
	 */
	public BigDecimal F16 = BigDecimal.ZERO;

	/**
	 * 金额逾期率
	 */
	public BigDecimal F17 = BigDecimal.ZERO;

	/**
	 * 逾期笔数
	 */
	public int F18;

	/**
	 * 项目逾期率
	 */
	public BigDecimal F19 = BigDecimal.ZERO;

	/**
	 * 累计代偿金额
	 */
	public BigDecimal F20 = BigDecimal.ZERO;
	/**
	 * 累计代偿笔数
	 */
	public int F21;

	/**
	 * 逾期90天（不含）以上金额
	 */
	public BigDecimal F22 = BigDecimal.ZERO;

	/**
	 * 逾期90天（不含）以上笔数
	 */
	public int F23;

	/**
	 * 更新时间
	 */
	public Timestamp F24;

}
