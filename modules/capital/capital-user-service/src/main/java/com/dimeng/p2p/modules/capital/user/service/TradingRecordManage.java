package com.dimeng.p2p.modules.capital.user.service;

import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.Date;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.TradingType;
import com.dimeng.p2p.modules.capital.user.service.entity.TradingRecordEntity;

/**
 * 交易记录
 */
public abstract interface TradingRecordManage extends Service {
	/**
	 * <dt>
	 * <dl>
	 * 获取账户余额
	 * </dl>
	 * <dl>
	 * <ol>
	 * T6023.F03字段
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return 账户余额
	 * @throws Throwable
	 */
	public abstract BigDecimal balance() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 获取可用资金
	 * </dl>
	 * <dl>
	 * <ol>
	 * T6023.F05字段
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return 可用资金
	 * @throws Throwable
	 */
	public abstract BigDecimal availableFunds() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 获取冻结资金
	 * </dl>
	 * <dl>
	 * <ol>
	 * T6023.F04字段
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return 冻结资金
	 * @throws Throwable
	 */
	public abstract BigDecimal freezeFunds() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 获取充值总额
	 * </dl>
	 * <dl>
	 * <ol>
	 * T6033.F04字段
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return 充值总额
	 * @throws Throwable
	 */
	public abstract BigDecimal rechargeFunds() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 获取提现总额
	 * </dl>
	 * <dl>
	 * <ol>
	 * T6034.F04字段
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @return 充值总额
	 * @throws Throwable
	 */
	public abstract BigDecimal withdrawFunds() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 分页查询交易记录
	 * </dl>
	 * <dl>
	 * <ol>
	 * <li>查询表T6032，字段F01，F03，F04，F05，F06，F07，F09</li>
	 * <li>按类型查询，等于匹配，若为空串或null则查询所有类型；</li>
	 * <li>按时间查询，范围查询，大于等于起始时间startTime小于等于endTime</li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param type
	 *            交易类型
	 * @param startTime
	 *            起始时间
	 * @param endTime
	 *            结束时间
	 * @param paging
	 *            分页对象
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<TradingRecordEntity> search(
			TradingType tradingType, Date startTime, Date endTime, Paging paging)
			throws Throwable;

	/**
	 * 导出交易记录查询数据
	 * 
	 * @param entities
	 *            记录列表
	 * @param outputStream
	 *            输出流
	 * @param charset
	 *            编码方式
	 * @throws Throwable
	 */
	public abstract void export(TradingRecordEntity[] entities,
			OutputStream outputStream, String charset) throws Throwable;

}
