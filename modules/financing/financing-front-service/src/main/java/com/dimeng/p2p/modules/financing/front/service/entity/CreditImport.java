package com.dimeng.p2p.modules.financing.front.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

public class CreditImport implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7055592259137457866L;
	
	/**
	 * 转让id
	 */
	public int zrId;
	
	/**
	 * 转出id
	 */
	public int zcId;
	
	/**
	 * 转让者id
	 */
	public int zrzId;
	
	/**
	 * 转入份数
	 */
	public int zrfs;
	
	/**
	 * 成交时间
	 */
	public String cjsj;
	
	/**
	 * 转让手术费
	 */
	public BigDecimal zrsxf = new BigDecimal(0);
	
	/**
	 * 格式合同号
	 */
	public String gshth;

}
