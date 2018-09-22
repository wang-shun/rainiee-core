/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.account.console.service.entity.Czgl;
import com.dimeng.p2p.modules.account.console.service.entity.UserRecharge;
import com.dimeng.p2p.modules.account.console.service.query.CzglRecordExtendsQuery;
import com.dimeng.p2p.modules.account.console.service.query.CzglRecordQuery;

/**
 * 用户充值管理
 * 
 * @author guopeng
 * 
 */
public abstract interface UserRechargeManage extends Service {
	/**
	 * 
	 * <dl>
	 * 描述：查询用户充值记录.
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<UserRecharge> serarch(CzglRecordQuery query,
			Paging paging) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：充值统计.
	 * </dl>
	 * 
	 * @return
	 * @throws Throwable
	 */
	public Czgl getRechargeInfo() throws Throwable;

	/**
	 * 查询成功充值总额
	 */
	public abstract BigDecimal getCzze() throws Throwable;

	/**
	 * 查询成功充值手续费
	 */
	public abstract BigDecimal getCzsxf() throws Throwable;

	/**
	 * 导出充值记录
	 * 
	 * @param records
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public void export(UserRecharge[] records, OutputStream outputStream,
			String charset) throws Throwable;
	
	/**
	 * 
	 * <dl>
	 * 描述：查询用户充值记录.
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<UserRecharge> serarch(CzglRecordExtendsQuery query,
			Paging paging) throws Throwable;
	
	/**
	 * 
	 * <dl>
	 * 描述：查询用户充值记录统计.
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract UserRecharge chargeRecordCount(CzglRecordExtendsQuery query) throws Throwable;
}
