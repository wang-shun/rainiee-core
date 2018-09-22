package com.dimeng.p2p.modules.finance.console.service;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.enums.TradeType;
import com.dimeng.p2p.modules.finance.console.service.entity.Dfsdzq;
import com.dimeng.p2p.modules.finance.console.service.entity.DfsdzqRecord;
import com.dimeng.p2p.modules.finance.console.service.query.DfsdzqRecordQuery;

public interface DfsdzqManage extends Service {

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：垫付所得债权统计记录
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
	 * <li>如果{@code advance > 0} 则
	 * <ol>
	 * <li>定义{@code List<Integer> loanIds = new ArrayList<>()}</li>
	 * <li>查询{@code T7030}表  查询条件{@code T7030.F02=advance AND (T7030.F07=}{@link TradeType#DF} {@code OR T7030.07=}{@link TradeType#DFHF}{@code )}
	 * 查询字段{@code T7030.F09}并添加到{@code loanIds}中
	 * </li>
	 * <li>查询{@code T7030}表  查询条件{@code T7030.F02=advance AND T7030.F07=}{@link TradeType#DF} 查询字段{@code SUM(T7030.F05)}赋值给{@link Dfsdzq#paymentAmount}</li>
	 * <li>查询{@code T7030}表  查询条件{@code T7030.F02=advance AND T7030.F07=}{@link TradeType#DFHF} 查询字段{@code SUM(T7030.F04)}赋值给{@link Dfsdzq#paymentRestore}</li>
	 * <li>调用{@code getDsAmount(loanIds)}结果赋值给{@link Dfsdzq#restoreAmount}</li>
	 * <li>{@link Dfsdzq#profit}等于{@link Dfsdzq#paymentRestore}减去{@link Dfsdzq#paymentAmount}</li>
	 * </ol>
	 * 否则 
	 * <ol>
	 * <li>定义{@code List<Integer> loanIds = new ArrayList<>()}</li>
	 * <li>查询{@code T7028}表  查询条件{@code T7028.F06= }{@link TradeType#DF  } {@code OR T7028.F06=}{@link TradeType#DFHF}
	 * 查询字段{@code T7028.F08}并添加到{@code loanIds}中
	 * </li>
	 * <li>查询{@code T7027}表  查询字段{@code T7027.F04}赋值给{@link Dfsdzq#profit}</li>
	 * <li>查询{@code T7028}表  查询条件{@code T7028.F06= }{@link TradeType#DF} 查询字段{@code SUM(T7028.F04)}赋值给{@link Dfsdzq#paymentAmount}</li>
	 * <li>查询{@code T7028}表  查询条件{@code T7028.F06= }{@link TradeType#DFHF} 查询字段{@code SUM(T7028.F03)}赋值给{@link Dfsdzq#paymentRestore}</li>
	 * <li>调用{@code getDsAmount(loanIds)}结果赋值给{@link Dfsdzq#restoreAmount}</li>
	 * <li>{@link Dfsdzq#profit}等于{@link Dfsdzq#paymentRestore}减去{@link Dfsdzq#paymentAmount}</li>
	 * </ol>
	 * </li>
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param advance  机构ID
	 * @return  {@link Dfsdzq} 垫付所得债权对象
	 * @throws Throwable
	 */
	public Dfsdzq getPlatformPaymentInfo(int advance) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 查询垫付所得债权列表
	 * </dl>
	 * </dt>
	 * 
	 * 
	 * @param advance 机构ID
	 * @param query  查询条件
	 * @param paging 分页参数
	 * @return {@link PagingResult}{@code <}{@link DfsdzqRecord}{@code >} 分页查询结果,没有结果则返回{@code null}
	 * @throws Throwable
	 */
	
	
	public PagingResult<DfsdzqRecord> search(int advance,
			DfsdzqRecordQuery query, Paging paging) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述： 导出垫付所得债权记录
	 * </dl>
	 * </dt>
	 * 
	 * @param platformPaymentRecord
	 *            垫付所得债权分页集合
	 * @param outputStream
	 *            字节输出流
	 * @param charset
	 *            垫付所得债权分页集合
	 * 
	 * @throws Throwable
	 */
	public void export(DfsdzqRecord[] records, OutputStream outputStream,
			String charset) throws Throwable;

}
