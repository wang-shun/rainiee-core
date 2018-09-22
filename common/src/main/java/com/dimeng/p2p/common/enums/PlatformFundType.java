package com.dimeng.p2p.common.enums;

/**
 * 平台资金类型.
 * 
 */
public enum PlatformFundType {

	CZSXF("充值手续费"), TXSXF("提现手续费"), CZCB("充值成本"), TXCB("提现成本"), JKGLF("借款管理费"), YQGLF(
			"逾期管理费"), ZQZRFY("债权转让费用"), SFYZSXF("身份验证手续费"), HDFY("活动费用"), YXTGJL(
			"有效推广奖励"), TGCXJL("推广持续奖励"), YXLCJHZBCG("优选理财计划招标成功"), PTZHCZ(
			"平台账户充值"), PTZHTX("平台账户提现"), YXLCJHHK("优选理财计划还款"), YXLCFWF(
			"优选理财服务费"), YXLCJRF("优选理财加入费"), YXLCTCF("优选理财退出费");

	protected final String name;

	private PlatformFundType(String name) {
		this.name = name;
	}

	/**
	 * 获取中文名称.
	 * 
	 * @return {@link String}
	 */
	public String getName() {
		return name;

	}
}
