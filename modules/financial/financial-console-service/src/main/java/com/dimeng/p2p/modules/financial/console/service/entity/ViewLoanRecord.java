package com.dimeng.p2p.modules.financial.console.service.entity;

import com.dimeng.p2p.common.enums.Sex;

/**
 * 借款详情
 * 
 * @author gongliang
 * 
 */
public class ViewLoanRecord {

	/**
	 * 用户Id
	 */
	public int userId;

	/**
	 * 真实姓名
	 */
	public String userName;

	/**
	 * 年龄
	 */
	public int age;

	/**
	 * 学校
	 */
	public String graduateSchool;

	/**
	 * 公司行业
	 */
	public String companyBusiness;

	/**
	 * 公司规模
	 */
	public String companyScale;

	/**
	 * 职位
	 */
	public String position;

	/**
	 * 工作城市
	 */
	public String workCity;

	/**
	 * 审核状态
	 */
	public AttestationCheck[] attestationCheck;

	/**
	 * 性别
	 */
	public Sex sex;
}
