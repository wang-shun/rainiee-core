package com.dimeng.p2p.common.entities;

import java.math.BigDecimal;

/**
 * 电子协议还款计划
 * @author gaoshaolong
 *
 */
public class DzxyHkjh {
	/**
	 * 还款日
	 */
	public String hkrq;
	/**
	 * 还款金额
	 */
	public BigDecimal hkje = new BigDecimal(0);
	/**
	 * 还款类型
	 */
	public String hklx;
	
	
	public String getHkrq() {
		return hkrq;
	}
	public void setHkrq(String hkrq) {
		this.hkrq = hkrq;
	}
	public BigDecimal getHkje() {
		return hkje;
	}
	public void setHkje(BigDecimal hkje) {
		this.hkje = hkje;
	}
	public String getHklx() {
		return hklx;
	}
	public void setHklx(String hklx) {
		this.hklx = hklx;
	}
	
	
}
