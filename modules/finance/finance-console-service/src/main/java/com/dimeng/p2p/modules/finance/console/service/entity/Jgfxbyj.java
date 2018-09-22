package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.common.enums.OrganizationType;

public class Jgfxbyj {

	public int id;
	/**
	 * 机构名称
	 */
	public String organizationName;
	/**
	 * 机构类型
	 */
	public OrganizationType type;
	/**
	 * 机构信用额度
	 */
	public BigDecimal organizationCreditAmount=new BigDecimal(0);
	/**
	 * 机构可用额度
	 */
	public BigDecimal organizationUsableAmount=new BigDecimal(0);

	/**
	 * 原始备用总额
	 */
	public BigDecimal originalReserveAmount=new BigDecimal(0);

	/**
	 * 备用金余额
	 */
	public BigDecimal originalReserveBalance=new BigDecimal(0);

}
