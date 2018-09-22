package com.dimeng.p2p.repeater.policy.entity;

import java.io.Serializable;

/**
 * 投资/借款用户分布
 */
public class AgeDistributionEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 年龄段
	 */
	public String ageRanage;
	/**
	 * 人数
	 */
	public int number ;
}
