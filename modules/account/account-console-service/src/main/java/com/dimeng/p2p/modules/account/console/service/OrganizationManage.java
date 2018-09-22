package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.account.console.service.entity.Organization;
import com.dimeng.p2p.modules.account.console.service.query.EnterpriseQuery;

public abstract interface OrganizationManage extends Service {
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：添加机构信息
	 * </dl>
	 * 
	 * @param user
	 *            添加信息
	 * @throws Throwable
	 */
	public abstract void add(Organization enterprise) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：修改机构信息
	 * </dl>
	 * 
	 * @param user
	 *            修改信息
	 * @throws Throwable
	 */
	public abstract String update(Organization enterprise) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：查询所机构人用户列表.
	 * </dl>
	 * 
	 * @param userQuery
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Organization> enterpriseSearch(EnterpriseQuery enterpriseQuery, Paging paging) throws Throwable;
	

	/**
	 * <dt>
	 * <dl>
	 * 描述：导出机构用户信息
	 * </dl>
	 * 
	 * @param users
	 *            用户信息列表
	 * @param outputStream
	 *            输出流
	 * @param charset
	 *            字符编码
	 * @throws Throwable
	 */
	public abstract void export(Organization[] enterprises,
			OutputStream outputStream, String charset) throws Throwable;
	/**
	 * 查询机构基础信息
	 */
	public abstract Organization getEnterprise(int userId) throws Throwable;

}
