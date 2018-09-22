package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6032_F03;

/**
 * 用户资金交易记录
 */
public class T6032 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户资金交易记录自增ID
	 */
	public int F01;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F02;

	/**
	 * 交易类型,CZ:充值;TBCG:投资成功;ZBCG:招标成功;CJFWF:成交服务费;HSBX:回收本息;CHBX:偿还本息;TQHK:提前还款;
	 * TQHS :提前回收;JKGLF:借款管理费;YQGLF:逾期管理费;CGTX:成功提现;TXSXF:提现手续费;SFYZSXF:身份验证手续费;
	 * DBFY :担保费用;SDSHF:实地审核费用;WTDCFY:委托待查费用;HFFWF:返还服务费;GMZQ:购买债权;CSZQ:出售债权;
	 * ZQZRSXF: 债权转让手续费;HDJL:活动奖励;YXTGJL:有效推广奖励;CXTGJL:持续推广奖励;JRYXLCJH:加入优选理财计划;
	 * YXLCJHHK:
	 * 优选理财计划回款;YXLCJRF:优选理财加入费;YXLCFWF:优选理财服务费;YXLCTCF:优选理财退出费;XXCZ:线下充值;
	 */
	public T6032_F03 F03;

	/**
	 * 时间
	 */
	public Timestamp F04;

	/**
	 * 收入
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 支出
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 余额
	 */
	public BigDecimal F07 = BigDecimal.ZERO;

	/**
	 * 参考ID,具体见不同类型
	 */
	public int F08;

	/**
	 * 备注
	 */
	public String F09;

}
