package com.dimeng.p2p.repeater.score.entity;

import java.sql.Timestamp;

/**
 * 用户积分兑换记录（用户中心）
 * @author heluzhu
 *
 */
public class UserScoreOrderInfoVO {
	/**
	 * 商品名称
	 */
	public String proName;
	/**
	 * 兑换时间
	 */
	public Timestamp orderTimes;
	
	/**
	 * 消费积分
	 */
	public int castScore;
	
	/**
	 * 状态
	 */
	public String status;

}
