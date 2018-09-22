package com.dimeng.p2p.account.user.service.entity;

/**
 * 用户信息
 * @author Administrator
 *
 */
public interface UserBaseQuery {
	/**
	 * 用户ID
	 */
	public int getAcount();
	/**
	 * 最高学历
	 */
	public String getEducation();
	
	/**
	 * 毕业院校
	 */
	public String getSchool();
	
	/**
	 * 结婚状况
	 */
	public String getMarriage();
	
	/**
	 * 居住地址
	 */
	public String getAddress();
	
	/**
	 * 公司行业
	 */
	public String getCompanyhy();
	
	/**
	 * 公司规模
	 */
	public String getCompangm();
	
	/**
	 * 职位名称
	 */
	public String getJobtitle();
	
	/**
	 * 收入
	 */
	public String getIncome();
}
