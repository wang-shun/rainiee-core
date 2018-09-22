package com.dimeng.p2p.modules.account.console.service;

import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.account.console.service.entity.OfflineChargeImportRecord;
import com.dimeng.p2p.modules.account.console.service.entity.OfflineChargeRecord;
import com.dimeng.p2p.modules.account.console.service.query.OfflineChargeExtendsQuery;
import com.dimeng.p2p.modules.account.console.service.query.OfflineChargeQuery;

public abstract interface OfflineChargeManage extends Service {

	/**
	 * 
	 * 描述：分页查询线下充值列表
	 * 
	 * @param query
	 *            线下充值查询接口
	 * @param page
	 *            分页对象
	 * @return {@link PagingResult}{@code <}{@link query}{@code >} 充值分页集合
	 * @throws Throwable
	 */
	public PagingResult<OfflineChargeRecord> search(OfflineChargeQuery query,
			Paging paging) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：新增线下充值.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>...</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param accountName
	 *            账户名称
	 * @param amount
	 *            充值金额
	 * @param remark
	 *            备注
	 * @return 记录ID
	 * @throws Throwable
	 */
	public int add(String accountName, BigDecimal amount, String remark)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：审核通过.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>...</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param id
	 *            线下充值记录ID
	 * @param passed
	 *            是否审核通过
	 * @throws Throwable
	 */
	public void check(int id, boolean passed) throws Throwable;
	
	
	   /**
     * <dt>
     * <dl>
     * 描述：审核通过.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 逻辑校验：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 业务处理：
     * <ol>
     * <li>...</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param id
     *            线下充值记录ID
     * @param passed
     *            是否审核通过
     * @throws Throwable
     */
    public int checkCharge(int id, boolean passed) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：.
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * <li>...</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 返回结果说明：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param xxczId
	 * @throws Throwable
	 */
	public void cancel(int xxczId) throws Throwable;

	/**
	 * 导出线下充值记录
	 * 
	 * @param records
	 * @param outputStream
	 * @param charset
	 * @throws Throwable
	 */
	public void export(OfflineChargeRecord[] records,
			OutputStream outputStream, String charset) throws Throwable;
	
	/**
	 * 
	 * 描述：分页查询线下充值列表
	 * 
	 * @param query
	 *            线下充值查询接口
	 * @param page
	 *            分页对象
	 * @return {@link PagingResult}{@code <}{@link query}{@code >} 充值分页集合
	 * @throws Throwable
	 */
	public PagingResult<OfflineChargeRecord> search(OfflineChargeExtendsQuery query,
			Paging paging) throws Throwable;
	
	/**
	 * 
	 * 描述：统计线下充值金额
	 * 
	 * @param query
	 *            线下充值查询接口
	 * @return {@link PagingResult}{@code <}{@link query}{@code >} 充值分页集合
	 * @throws Throwable
	 */
	public BigDecimal xxczAmountCount(OfflineChargeExtendsQuery query) throws Throwable;

	/**
	 * 平台线下充值（没有实际资金的流动）
	 * @param amount
	 * @throws Throwable
	 */
	public void ptCharge(BigDecimal amount)throws Throwable;

	/**
	 * 平台线下充值（没有实际资金的流动）
	 * @param amount
	 * @throws Throwable
	 */
	public void ptWithdraw(BigDecimal amount)throws Throwable;
	
	/**
     * 描述： 查询用户名是否存在.
     * @param accountName
     * @return
     * @throws Throwable
     */
    public abstract boolean isAccountExists(String accountName)
            throws Throwable;

	/**
	 * 批量导入线下充值记录
	 * @param inputStream
	 * @param contentType
	 * @return
	 * @throws Throwable
	 */
	public List<OfflineChargeImportRecord> batchImportRecord(InputStream inputStream, String contentType) throws Throwable;

}
