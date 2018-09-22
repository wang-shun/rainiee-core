package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7026_F06;

/**
 * 平台资金流水表
 */
public class T7026 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 平台资金流水表自增ID
	 */
	public int F01;

	/**
	 * 时间
	 */
	public Timestamp F02;

	/**
	 * 收入
	 */
	public BigDecimal F03 = BigDecimal.ZERO;

	/**
	 * 支出
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 余额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 类型,CZSXF:充值手续费;TXSXF:提现手续费;CZCB:充值成本;TXCB:提现成本;JKGLF:借款管理费;YQGLF:逾期管理费;
	 * ZQZRFY :债权转让费用;SFYZSXF:身份验证手续费;HDFY:活动费用;YXTGJL:有效推广奖励;TGCXJL:推广持续奖励;
	 * YXLCJHZBCG :优选理财计划招标成功
	 * ;PTZHCZ:平台账户充值;PTZHTX:平台账户提现;YXLCJHHK:优选理财计划还款;YXLCJRF:优选理财加入费;
	 * YXLCFWF:优选理财服务费;YXLCTCF:优选理财退出费;XXCZ:线下充值;
	 */
	public T7026_F06 F06;

	/**
	 * 备注
	 */
	public String F07;

	/**
	 * 引用ID,根据类型引用不同表ID
	 */
	public int F08;

	/**
	 * 用户ID,参考T6010.F01
	 */
	public int F09;

}
