package com.dimeng.p2p.S61.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 企业基础信息
 */
public class T6161 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID,参考T6110.F01
	 */
	public int F01;

	/**
	 * 企业编号
	 */
	public String F02;

	/**
	 * 营业执照登记注册号,唯一
	 */
	public String F03;

	/**
	 * 企业名称
	 */
	public String F04;

	/**
	 * 企业纳税号
	 */
	public String F05;

	/**
	 * 组织机构代码
	 */
	public String F06;

	/**
	 * 注册年份
	 */
	public int F07;

	/**
	 * 注册资金(万元)
	 */
	public BigDecimal F08 = BigDecimal.ZERO;

	/**
	 * 行业
	 */
	public String F09;

	/**
	 * 企业规模,单位: 人
	 */
	public int F10;

	/**
	 * 法人
	 */
	public String F11;

	/**
	 * 法人身份证号,9-16位星号替换
	 */
	public String F12;

	/**
	 * 法人身份证号,加密存储
	 */
	public String F13;

	/**
	 * 资产净值(万元)
	 */
	public BigDecimal F14 = BigDecimal.ZERO;

	/**
	 * 上年度经营现金流入(万元)
	 */
	public BigDecimal F15 = BigDecimal.ZERO;

	/**
	 * 贷款卡证书编号
	 */
	public String F16;

	/**
	 * 企业信用证书编号
	 */
	public String F17;

	/**
	 * 企业简称
	 */
	public String F18;

	/**
	 * 社会信用代码
	 */
	public String F19;

	/**
	 * 是否三证合一
	 */
	public String F20;

	/**
	 * 担保机构描述
	 */
	public String jgmx;

	/**
	 * 担保情况描述
	 */
	public String qkmx;

	/**
	 * 开户银行许可证号
	 */
	public String F21;

	/**
	 * 营业执照所在地
	 */
	public String F23;

	/**
	 * 营业执照到期日
	 */
	public String F24;

}
