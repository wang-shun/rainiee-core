package com.dimeng.p2p.modules.finance.console.service;

import java.io.OutputStream;
import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.finance.console.service.entity.Ptzjgl;
import com.dimeng.p2p.modules.finance.console.service.entity.PtzjglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.PtzjglRecordQuery;

public interface PtzjManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：平台资金统计记录
	 * </dl>
	 * </dt>
	 * 
	 * @return platformFund 平台资金对象
	 * @throws Throwable
	 */
	public Ptzjgl getPlatFormFundInfo() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询平台资金列表
	 * </dl>
	 * </dt>
	 * 
	 * @param query
	 *            平台资金查询接口
	 * @param page
	 *            分页对象
	 * @return PagingResult<UserFundRecord> 平台资金分页集合
	 * @throws Throwable
	 */
	public PagingResult<PtzjglRecord> search(PtzjglRecordQuery query,
			Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出平台资金记录
	 * </dl>
	 * </dt>
	 * 
	 * @param platformFundRecord
	 *            平台资金分页集合
	 * @param outputStream
	 *            字节输出流
	 * @param charset
	 *            平台资金分页集合
	 * @return
	 * 
	 * @throws Throwable
	 */
	public void export(PtzjglRecord[] records, OutputStream outputStream,
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
