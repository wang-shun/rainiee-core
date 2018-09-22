package com.dimeng.p2p.account.user.service.entity;

import com.dimeng.p2p.S61.entities.T6103;

/**
 * 我的体验金
 * @author Administrator
 *
 */
public class MyExperienceRecod extends T6103 {
    
    private static final long serialVersionUID = 1L;
	
	/**
	 * 体验金金额
	 */
	public String expAmount;

	/**
	 * 赠送时间
	 */
	public String beginDate;

	/**
	 * 过期时间
	 */
	public String endDate;

	/**
	 * 体验金状态
	 */
	public String state;

	/**
	 * 来源
	 */
	public String fromStr;
	
}
