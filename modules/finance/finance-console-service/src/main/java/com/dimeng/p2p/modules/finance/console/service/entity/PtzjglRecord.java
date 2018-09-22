package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.PlatformFundType;

public class PtzjglRecord {

	/**
	 * 用户名
	 */
	public String loginName;

	/**
	 * 时间
	 */
	public Timestamp playTime;
	/**
	 * 类型明细
	 */
	public PlatformFundType type;
	/**
	 * 收入
	 */
	public BigDecimal income=new BigDecimal(0);
	/**
	 * 支出
	 */
	public BigDecimal replay=new BigDecimal(0);

	/**
	 * 结余
	 */
	public BigDecimal remain=new BigDecimal(0);
	/**
	 * 备注
	 */
	public String remark;
}
