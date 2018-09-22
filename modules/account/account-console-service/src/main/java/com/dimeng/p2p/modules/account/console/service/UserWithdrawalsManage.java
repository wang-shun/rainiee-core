/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.account.console.service.entity.Bank;
import com.dimeng.p2p.modules.account.console.service.entity.Txgl;
import com.dimeng.p2p.modules.account.console.service.entity.UserWithdrawals;
import com.dimeng.p2p.modules.account.console.service.query.TxglRecordQuery;

/**
 * 用户提现管理
 * 
 * @author guopeng
 * 
 */
public abstract interface UserWithdrawalsManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述： 提现统计记录
	 * </dl>
	 * </dt>
	 * 
	 * @return Extraction 提现实体对象
	 * @throws Throwable
	 */
	public Txgl getExtractionInfo() throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：提现记录查询列表.
	 * </dl>
	 * 
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<UserWithdrawals> search(TxglRecordQuery query,
			Paging paging) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：查询提现记录详情.
	 * </dl>
	 * 
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract UserWithdrawals get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 批量审核提现记录
	 * </dl>
	 * </dt>
	 * 
	 * @param passed
	 *            是否放款
	 * @param check_reason
	 *            审核不通过意见
	 * @param ids
	 *            提现申请ID列表
	 * @throws Throwable
	 */
	public void check(boolean passed, String check_reason, int... ids)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 批量放款
	 * </dl>
	 * </dt>
	 * 
	 * @param passed
	 *            是否放款
	 * @param check_reason
	 *            拒绝放款意见
	 * @param ids
	 *            提现申请ID列表
	 * @return orderIds 放款通过生成的订单ID列表
	 * @throws Throwable
	 */
	public int[] fk(boolean passed, String check_reason, int... ids)
			throws Throwable;

	/**
	 * 查询所有银行卡
	 * 
	 * @return
	 * @throws Throwable
	 */
	public Bank[] getBanks() throws Throwable;

	/**
	 * 导入提现
	 * 
	 * @param inputStream
	 * @param real_name
	 * @param charset
	 * @throws Throwable
	 */
	public void importData(InputStream inputStream, String real_name,
			String charset) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出提现审核通过
	 * </dl>
	 * </dt>
	 * 
	 * @param txglRecord
	 *            审核通过记录
	 * @throws Throwable
	 */
	public void export(UserWithdrawals[] txglRecord, OutputStream outputStream,
			String charset) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出已提现
	 * </dl>
	 * </dt>
	 * 
	 * @param txglRecord
	 *            审核通过记录
	 * @throws Throwable
	 */
	public void exportYtx(UserWithdrawals[] txglRecord,
			OutputStream outputStream, String charset) throws Throwable;
	
	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出已提现。这个方法跟上面那个是exportYtx是重载方法。
	 * </dl>
	 * </dt>
	 * 
	 * @param txglRecord
	 *            审核通过记录
	 * @throws Throwable
	 */
	public void exportYtxContent(UserWithdrawals[] txglRecord,
			OutputStream outputStream, String charset) throws Throwable;

	/**
	 * 提现成功总额
	 */
	public abstract BigDecimal getTxze() throws Throwable;

	/**
	 * 提现成功手续费
	 */
	public abstract BigDecimal getTxsxf() throws Throwable;
}
