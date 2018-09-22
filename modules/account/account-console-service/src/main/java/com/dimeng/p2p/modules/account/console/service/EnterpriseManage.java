/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;
import java.sql.Connection;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6112;
import com.dimeng.p2p.S61.entities.T6113;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S61.entities.T6162;
import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.S61.entities.T6164;
import com.dimeng.p2p.S61.entities.T6180;
import com.dimeng.p2p.modules.account.console.service.entity.AT6161;
import com.dimeng.p2p.modules.account.console.service.entity.Enterprise;
import com.dimeng.p2p.modules.account.console.service.entity.Jg;
import com.dimeng.p2p.modules.account.console.service.query.EnterpriseQuery;

/**
 * 企业用户信息
 * 
 * @author guopeng
 * 
 */
public abstract interface EnterpriseManage extends Service {
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：添加企业信息
	 * </dl>
	 * 
	 * @param user
	 *            添加信息
	 * @throws Throwable
	 */
	public abstract int add(Enterprise enterprise) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：修改企业信息
	 * </dl>
	 * 
	 * @param user
	 *            修改信息
	 * @throws Throwable
	 */
	public abstract void update(Enterprise enterprise) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：查询所企业人用户列表.
	 * </dl>
	 * 
	 * @param userQuery
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Enterprise> enterpriseSearch(
			EnterpriseQuery enterpriseQuery, Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：导出企业用户信息
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
	public abstract void export(Enterprise[] enterprises,
			OutputStream outputStream, String charset) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：查询所有机构.
	 * </dl>
	 * 
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract Jg[] searchJg() throws Throwable;
	
	/**
	 * 
	 * <dl>
	 * 描述：查询已通过第三方审核的机构
	 * </dl>
	 * 
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract Jg[] searchTgJg() throws Throwable;

	/**
	 * 根据机构ID查询机构描述信息
	 */
	public abstract String getJgDes(int id) throws Throwable;
	
	/**
	 * 根据机构ID查询机构描述信息
	 */
	public abstract T6180 getJgInfo(int id) throws Throwable;

	/**
	 * 查询企业基础信息
	 */
	public abstract AT6161 getEnterprise(int userId) throws Throwable;

	/**
	 * 根据企业编号查询企业基础信息
	 */
	public abstract T6161 getEnterpriseByBH(String bhId) throws Throwable;

	/**
	 * 查询企业财务状况
	 */
	public abstract T6163[] getEnterpriseCw(int userId) throws Throwable;

	/**
	 * 查询企业介绍信息
	 */
	public abstract T6162 getEnterpriseJs(int userId) throws Throwable;

	/**
	 * 查询企业联系人
	 */
	public abstract T6164 getEnterpriseLxr(int userId) throws Throwable;

	/**
	 * 修改企业基础资料
	 */
	public abstract String updateEnterpriseBase(AT6161 t6161) throws Throwable;

	/**
	 * 修改企业信息
	 */
	public abstract void updateEnterpriseOtherInfos(T6161 t6161, T6162 t6162,
			T6163[] t6163, T6164 t6164) throws Throwable;
	/**
	 * 插入或更新房产信息
	 * @throws Throwable
	 */
   public  abstract int addOrupdateHourse(T6112 entity) throws Throwable;
	/**
	 * 插入或更新车产信息
	 * @throws Throwable
	 */
  public  abstract int addOrupdateCar(T6113 entity) throws Throwable;
  
  public abstract T6112[] getHourseInfo(int userId) throws Throwable;
  
  public abstract T6113[] getCarInfo(int userId) throws Throwable;
}
