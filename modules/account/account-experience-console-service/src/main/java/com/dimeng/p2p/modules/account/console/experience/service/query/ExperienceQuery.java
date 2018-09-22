package com.dimeng.p2p.modules.account.console.experience.service.query;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6103_F06;
import com.dimeng.p2p.S61.enums.T6103_F08;

public abstract interface ExperienceQuery {
	/**
	 * 用户名
	 * 
	 * @return String
	 */
	public String userName();

	/**
	 * 生效开始时间
	 */
	public Timestamp validStartTime();

	/**
	 * 生效结束时间
	 */
	public Timestamp validEndTime();

	/**
	 * 失效开始时间
	 */
	public Timestamp invalidStartTime();

	/**
	 * 失效结束时间
	 */
	public Timestamp invalidEndTime();

	/**
	 * 利息生效开始时间
	 */
	public Timestamp lixiStartTime();

	/**
	 * 利息生效结束时间
	 */
	public Timestamp lixiEndTime();

	/**
	 * 体检金状态
	 * 
	 * @return
	 */
	public T6103_F06 status();

	/**
	 * 体验金来源
	 */
	public T6103_F08 source();

    /**
     * 体验金金额开始
     */
    public BigDecimal beginAmount();

    /**
     * 体验金金额结束
     */
    public BigDecimal endAmount();

    /**
     * 操作人
     * @return
     */
    public String openName();
}
