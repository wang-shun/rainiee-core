package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;
import java.util.List;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S62.entities.T6231;

/**
 * 电子协议-抵押
 * @author gaoshaolong
 *
 */
public class DzxyDy {
	/**
	 * 协议编号
	 */
	public String xy_no;
	/**
	 * 出借人列表
	 */
	public List<CjrList> cjrList;
	/**
	 * 乙方借款人
	 */
	public String yf_loginName;
	/**
	 * 丙方担保公司
	 */
	public String bf_companyName;
	/**
	 * 丙方地址
	 */
	public String bf_address;
	/**
	 * 还款日（哪天）
	 */
	public int jk_hkr;
	/**
	 * 还款计划列表
	 */
	public DzxyHkjh[] hkjh;
	/**
	 * 借款本金数额（大写）
	 */
	public String jk_money_dx;
	/**
	 * 标的主信息
	 */
	public Bdxq bdxq;
	/**
	 * 标的扩展信息
	 */
	public T6231 t6231;
	/**
	 * 借款用户信息
	 */
	public T6110 t6110;

	/**
	 * 个人借款用户基本信息
	 */
	public T6141 t6141;
	
	/**
	 * 企业借款用户基本信息
	 */
	public T6161 t6161;

	/**
	 * 丁方居间平台服务商
	 */
	public String df_companyName;
	/**
	 * 丁方地址
	 */
	public String df_address;
	/**
	 * 借款用途
	 */
	//public String jk_jkyt;
	/**
	 * 借款本金数额（小写）
	 */
	//public String jk_money_xx;
	/**
	 * 借款年化利率
	 */
	//public BigDecimal jk_rate = new BigDecimal(0);
	/**
	 * 借款期限
	 */
	//public int jk_jkqx;
	/**
	 * 还款分期月数
	 */
	//public int jk_hkqs;
	/**
	 * 逾期费率
	 */
	public BigDecimal yq_rate= new BigDecimal(0);
	/**
	 * 违约金费率
	 */
	public BigDecimal wyj_rate = new BigDecimal(0);
}
