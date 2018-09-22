package com.dimeng.p2p.modules.finance.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.finance.console.service.entity.JgbyjStatistics;
import com.dimeng.p2p.modules.finance.console.service.entity.Jgfxbyj;
import com.dimeng.p2p.modules.finance.console.service.entity.JgfxbyjDetail;
import com.dimeng.p2p.modules.finance.console.service.entity.JgfxbyjRecord;
import com.dimeng.p2p.modules.finance.console.service.entity.Jgxyjl;
import com.dimeng.p2p.modules.finance.console.service.entity.Jgyw;
import com.dimeng.p2p.modules.finance.console.service.entity.JgywRecord;
import com.dimeng.p2p.modules.finance.console.service.query.JgfxbyjglDetailQuery;
import com.dimeng.p2p.modules.finance.console.service.query.JgfxbyjglQuery;
import com.dimeng.p2p.modules.finance.console.service.query.JgywmxQuery;

public interface JgbyjglManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：获取机构风险备用金记录
	 * </dl>
	 * </dt>
	 * 
	 * @return
	 * @throws Throwable
	 */
	public JgbyjStatistics getJgfxbyj() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询机构风险备用金列表
	 * </dl>
	 * </dt>
	 * 
	 * @param query
	 *            机构风险备用金查询接口
	 * @param page
	 *            分页对象
	 * @return PagingResult<Jgfxbyjgl> 机构风险备用金分页集合
	 * @throws Throwable
	 */
	public PagingResult<Jgfxbyj> search(JgfxbyjglQuery query, Paging paging)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出机构风险备用金记录
	 * </dl>
	 * </dt>
	 * 
	 * @param 机构风险备用金分页集合
	 * @return
	 * 
	 * @throws Throwable
	 */
	public void export(Jgfxbyj[] records, OutputStream outputStream,
			String charset) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：获取机构风险备用金明细
	 * </dl>
	 * </dt>
	 * 
	 * @return 机构风险备用金对象
	 * @throws Throwable
	 */
	public JgfxbyjRecord getJgfxbyjRecord(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询机构风险备用金明细列表
	 * </dl>
	 * </dt>
	 * 
	 * @param query
	 *            机构风险备用金查询接口
	 * @param page
	 *            分页对象
	 * @return PagingResult<JgfxbyjglDetail> 机构风险备用金分页集合
	 * @throws Throwable
	 */
	public PagingResult<JgfxbyjDetail> serarchDetail(
			JgfxbyjglDetailQuery query, Paging page) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出机构风险备用金明细
	 * </dl>
	 * </dt>
	 * 
	 * @param Jgfxbyj
	 *            机构风险备用金分页集合
	 * @return
	 * 
	 * @throws Throwable
	 */
	public void export(JgfxbyjDetail[] records, OutputStream outputStream,
			String charset) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 充值
	 * </dl>
	 * </dt>
	 * 
	 * @param PtfxbyjglRecord
	 *            平台风险备用金分页集合
	 * @return
	 * 
	 * @throws Throwable
	 */
	public abstract int recharge(int id, BigDecimal ammount, String remark)
			throws Throwable;

	/**
	 * 提现
	 * 
	 * @param ammount
	 * @return
	 * @throws Throwable
	 */
	public abstract int withdrawal(int id, BigDecimal ammount, String remark)
			throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：统计机构业务明细
	 * </dl>
	 * </dt>
	 * 
	 * @param jgyw
	 *            机构业务对象
	 * @throws Throwable
	 */
	public Jgyw getJgyw(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询机构业务明细
	 * </dl>
	 * </dt>
	 * 
	 * @param JgywRecord
	 *            机构业务明细对象
	 * @return
	 * 
	 * @throws Throwable
	 */
	public PagingResult<JgywRecord> searchJgywmx(JgywmxQuery query,
			Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出机构业务明细
	 * </dl>
	 * </dt>
	 * 
	 * @param JgywRecord
	 *            机构业务明细对象
	 * @return
	 * 
	 * @throws Throwable
	 */
	public void export(JgywRecord[] records, OutputStream outputStream,
			String charset) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询机构信用记录
	 * </dl>
	 * </dt>
	 * 
	 * @param
	 * 
	 * @return Jgxyjl 机构信用记录对象
	 * @throws Throwable
	 */
	public Jgxyjl getJgxyjl(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：查询机构信用记录统计.
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
	 * @return
	 * @throws Throwable
	 */
	public Jgfxbyj getJgxbyj(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 调整信用额度
	 * </dl>
	 * </dt>
	 * 
	 * @param
	 * 
	 * @return Jgxyjl 机构信用记录对象
	 * @throws Throwable
	 */
	public void setJgxyed(int id, BigDecimal ammount) throws Throwable;
}
