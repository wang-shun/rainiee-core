package com.dimeng.p2p.modules.salesman.console.service.query;

import java.sql.Timestamp;

public interface CustomersQuery {

	/**
	 * 业务员编号
	 */
	public String ywyId();
	/**
	 * 联系电话
	 */
	public String tel();
	/**
	 * 姓名
	 */
	public String name();
	/**
	 * 开始时间
	 * @return
	 */
	public Timestamp startTime();
	/**
	 * 结束时间
	 * @return
	 */
	public Timestamp endTime();
}
