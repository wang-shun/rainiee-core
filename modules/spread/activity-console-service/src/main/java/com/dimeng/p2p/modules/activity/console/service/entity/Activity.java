package com.dimeng.p2p.modules.activity.console.service.entity;

import java.sql.Timestamp;

/**
 *活动信息
 */
public interface Activity {
	/**
	 * 活动主题
	 */
	public String title();
	/**
	 * 红包金额/加息利率
	 */
	public String[] money();
	/**
	 * 开始时间
	 */
	public Timestamp startTime();
	/**
	 * 结束时间
	 */
	public Timestamp endTime();
	
	/**
	 *发放数量
	 */
	public String[] num();
	/**
	 * 投资额度/倍数/最低充值额度
	 */
	public String[] edu();
	/**
	 * 最低投资金额（使用规则）
	 */
	public String[] leastInvest();
	
	/**
	 * 备注
	 * @return
	 */
	public String remark();
	
	/**
	 * 使用有效期
	 * @return
	 */
	public String[] syqx();
	
	/**
	 * 使用规则
	 * @return
	 */
	public int sygz();
	
	/**
	 * 是否首次充值
	 * @return
	 */
	public int sfsccz();
	
	/**
	 * 领取条件
	 * @return
	 */
	public String litj();
	
	/**
	 * 指定用户
	 * @return
	 */
	public String userId();
	
	/**
	 * 奖励类型
	 * @return
	 */
	public String jlType();
	
	/**
	 * 活动类型
	 * @return
	 */
	public String hdType();

	/**
	 * 使用期限类型
	 * @return
	 */
	public String[] syqxType();

	/**
	 * 体验金投资有效收益期计算方式(true:按月;false:按天)
	 * @return
	 */
	public String validMethod();

	/**
	 * 体验金投资有效收益期
	 * @return
	 */
	public String validDate();
}
