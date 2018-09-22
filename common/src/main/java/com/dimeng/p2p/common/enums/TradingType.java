package com.dimeng.p2p.common.enums;

/**
 * 交易类型
 */
public enum TradingType {
	CZ("充值"), TBCG("投资成功"), ZBCG("招标成功"), CJFWF("成交服务费"), HSBX("回收本息"), CHBX(
			"偿还本息"), TQHK("提前还款违约金"), TQHS("提前回收违约金"), JKGLF("借款管理费"), YQGLF(
			"逾期管理费"), CGTX("成功提现"), TXSXF("提现手续费"), SFYZSXF("身份验证手续费"), DBFY(
			"担保手续费"), SDSHF("实地审核费用"), WTDCFY("委托待查费用"), HFFWF("返还服务费"), GMZQ(
			"购买债权"), CSZQ("出售债权"), ZQZRSXF("债权转让手续费"), HDJL("活动奖励"), YXTGJL(
			"有效推广奖励"), CXTGJL("持续推广奖励"), JRYXLCJH("加入优选理财计划"), YXLCJHHK(
			"优选理财计划回款"), YXLCJRF("优选理财加入费"), YXLCFWF("优选理财服务费"), YXLCTCF(
			"优选理财退出费"), YXLCJHZBCG("优选理财计划招标成功"), XXCZ("线下充值");

	protected final String name;

	private TradingType(String name) {
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
