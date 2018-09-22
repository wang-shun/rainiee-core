package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

/**
 * 回帐查询
 * 
 */
public class BackOff {
	/**
	 * 待收本息
	 */
	public BigDecimal dsbx = new BigDecimal(0);
	/**
	 * 未来一个月
	 */
	public BigDecimal wlygy = new BigDecimal(0);
	/**
	 * 未来三个月
	 */
	public BigDecimal wlsgy = new BigDecimal(0);
	/**
	 * 未来一年
	 */
	public BigDecimal wlyn = new BigDecimal(0);

}
