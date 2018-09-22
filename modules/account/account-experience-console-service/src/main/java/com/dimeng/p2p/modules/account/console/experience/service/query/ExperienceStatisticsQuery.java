package com.dimeng.p2p.modules.account.console.experience.service.query;

import java.sql.Timestamp;

public abstract interface ExperienceStatisticsQuery {
	/**
	 * 根据用户名查询
	 * 
	 * @return
	 */
	public abstract String userName();

    /**
     * 标编号好
     * @return
     */
    public String bid();

    /**
     * 还款开始时间
     */
    public Timestamp repaymentStartTime();
    /**
     * 还款结束时间
     */
    public Timestamp repaymentEndTime();
}
