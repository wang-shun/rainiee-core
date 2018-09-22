package com.dimeng.p2p.S70.enums;

import com.dimeng.util.StringHelper;

/**
 * 类型
 */
public enum T7026_F06 {

	/**
	 * 优选理财退出费
	 */
	YXLCTCF("优选理财退出费"),

	/**
	 * 优选理财服务费
	 */
	YXLCFWF("优选理财服务费"),

	/**
	 * 优选理财加入费
	 */
	YXLCJRF("优选理财加入费"),

	/**
	 * 优选理财计划还款
	 */
	YXLCJHHK("优选理财计划还款"),

	/**
	 * 平台账户提现
	 */
	PTZHTX("平台账户提现"),

	/**
	 * 平台账户充值
	 */
	PTZHCZ("平台账户充值"),

	/**
	 * 优选理财计划招标成功
	 */
	YXLCJHZBCG("优选理财计划招标成功"),

	/**
	 * 推广持续奖励
	 */
	TGCXJL("推广持续奖励"),

	/**
	 * 有效推广奖励
	 */
	YXTGJL("有效推广奖励"),

	/**
	 * 活动费用
	 */
	HDFY("活动费用"),

	/**
	 * 身份验证手续费
	 */
	SFYZSXF("身份验证手续费"),

	/**
	 * 债权转让费用
	 */
	ZQZRFY("债权转让费用"),

	/**
	 * 逾期管理费
	 */
	YQGLF("逾期管理费"),

	/**
	 * 借款管理费
	 */
	JKGLF("借款管理费"),

	/**
	 * 提现成本
	 */
	TXCB("提现成本"),

	/**
	 * 充值成本
	 */
	CZCB("充值成本"),

	/**
	 * 提现手续费
	 */
	TXSXF("提现手续费"),

	/**
	 * 线下充值
	 */
	XXCZ("线下充值"),

	/**
	 * 充值手续费
	 */
	CZSXF("充值手续费");

	protected final String chineseName;

	private T7026_F06(String chineseName) {
		this.chineseName = chineseName;
	}

	/**
	 * 获取中文名称.
	 * 
	 * @return {@link String}
	 */
	public String getChineseName() {
		return chineseName;
	}

	/**
	 * 解析字符串.
	 * 
	 * @return {@link T7026_F06}
	 */
	public static final T7026_F06 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T7026_F06.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
