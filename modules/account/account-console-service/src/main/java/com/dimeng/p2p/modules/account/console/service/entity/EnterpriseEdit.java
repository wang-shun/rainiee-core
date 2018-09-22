package com.dimeng.p2p.modules.account.console.service.entity;


/**
 * 企业用户修改
 *
 */
public abstract interface EnterpriseEdit {
	
	/**
	 * 用户ID
	 */
	public abstract int getUserId();
	/**
	 * 企业税号
	 */
	public abstract String getDutyNumber();
	/**
	 * 企业名称
	 */
	public abstract String getName();
	
	/**
	 * 企业联系地址
	 */
	public abstract String getAddress();
	
	/**
	 * 联系人
	 */
	public abstract String getContacts();
	
	/**
	 * 联系人手机号码
	 */
	public abstract String getContactsMobile();
	
	/**
	 * 营业执照登记注册号
	 */
	public abstract String getLicenseNumber();
	
	/**
	 * 组织机构号
	 */
	public abstract String getOrganizationNumber();
	
	/**
	 * 邮箱地址
	 */
	public abstract String getEmail();
}
