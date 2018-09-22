package com.dimeng.p2p.modules.finance.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.finance.console.service.entity.Ptfxbyjgl;
import com.dimeng.p2p.modules.finance.console.service.entity.PtfxbyjglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.PtfxbyjglRecordQuery;

public interface PtfxbyjManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：获取平台风险备用金记录
	 * </dl>
	 * </dt>
	 * 
	 * @return PlatformFund 平台风险备用金对象
	 * @throws Throwable
	 */
	public Ptfxbyjgl getPlatformFundInfo() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询平台风险备用金列表
	 * </dl>
	 * </dt>
	 * 
	 * @param query
	 *            平台风险备用金查询接口
	 * @param page
	 *            分页对象
	 * @return PagingResult<PlatformReserveFundRecord> 平台风险备用金分页集合
	 * @throws Throwable
	 */
	public PagingResult<PtfxbyjglRecord> search(PtfxbyjglRecordQuery query,
			Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出平台风险备用金记录
	 * </dl>
	 * </dt>
	 * 
	 * @param PtfxbyjglRecord
	 *            平台风险备用金分页集合
	 * @return
	 * 
	 * @throws Throwable
	 */
	public void export(PtfxbyjglRecord[] records, OutputStream outputStream,
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
	public abstract int recharge(BigDecimal ammount, String remark)
			throws Throwable;

	/**
	 * 提现
	 * 
	 * @param ammount
	 * @return
	 * @throws Throwable
	 */
	public abstract int withdrawal(BigDecimal ammount, String remark)
			throws Throwable;

}
