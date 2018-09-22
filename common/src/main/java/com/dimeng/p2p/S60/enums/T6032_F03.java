package com.dimeng.p2p.S60.enums;

import com.dimeng.util.StringHelper;

/**
 * 交易类型
 */
public enum T6032_F03 {

	/**
	 * 优选理财服务费
	 */
	YXLCFWF("优选理财服务费"),

	/**
	 * 优选理财退出费
	 */
	YXLCTCF("优选理财退出费"),

	/**
	 * 优选理财加入费
	 */
	YXLCJRF("优选理财加入费"),

	/**
	 * 持续推广奖励
	 */
	CXTGJL("持续推广奖励"),

	/**
	 * 有效推广奖励
	 */
	YXTGJL("有效推广奖励"),

	/**
	 * 优选理财计划回款
	 */
	YXLCJHHK("优选理财计划回款"),

	/**
	 * 加入优选理财计划
	 */
	JRYXLCJH("加入优选理财计划"),

	/**
	 * 活动奖励
	 */
	HDJL("活动奖励"),

	/**
	 * 债权转让手续费
	 */
	ZQZRSXF("债权转让手续费"),

	/**
	 * 出售债权
	 */
	CSZQ("出售债权"),

	/**
	 * 购买债权
	 */
	GMZQ("购买债权"),

	/**
	 * 返还服务费
	 */
	HFFWF("返还服务费"),

	/**
	 * 委托待查费用
	 */
	WTDCFY("委托待查费用"),

	/**
	 * 实地审核费用
	 */
	SDSHF("实地审核费用"),

	/**
	 * 担保费用
	 */
	DBFY("担保费用"),

	/**
	 * 身份验证手续费
	 */
	SFYZSXF("身份验证手续费"),

	/**
	 * 提现手续费
	 */
	TXSXF("提现手续费"),

	/**
	 * 成功提现
	 */
	CGTX("成功提现"),

	/**
	 * 逾期管理费
	 */
	YQGLF("逾期管理费"),

	/**
	 * 借款管理费
	 */
	JKGLF("借款管理费"),

	/**
	 * 提前回收
	 */
	TQHS("提前回收"),

	/**
	 * 提前还款
	 */
	TQHK("提前还款"),

	/**
	 * 偿还本息
	 */
	CHBX("偿还本息"),

	/**
	 * 回收本息
	 */
	HSBX("回收本息"),

	/**
	 * 成交服务费
	 */
	CJFWF("成交服务费"),

	/**
	 * 招标成功
	 */
	ZBCG("招标成功"),

	/**
	 * 投资成功
	 */
	TBCG("投资成功"), CZSXF("CZSXF"),

	/**
	 * 线下充值
	 */
	XXCZ("线下充值"),

	/**
	 * 充值
	 */
	CZ("充值");

	protected final String chineseName;

	private T6032_F03(String chineseName) {
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
	 * @return {@link T6032_F03}
	 */
	public static final T6032_F03 parse(String value) {
		if (StringHelper.isEmpty(value)) {
			return null;
		}
		try {
			return T6032_F03.valueOf(value);
		} catch (Throwable t) {
			return null;
		}
	}
}
