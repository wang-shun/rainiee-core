package com.dimeng.p2p.modules.finance.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S60.entities.T6010;
import com.dimeng.p2p.S60.entities.T6011;
import com.dimeng.p2p.S60.entities.T6033;
import com.dimeng.p2p.S70.entities.T7022;
import com.dimeng.p2p.modules.finance.console.service.entity.Czgl;
import com.dimeng.p2p.modules.finance.console.service.entity.CzglRecord;
import com.dimeng.p2p.modules.finance.console.service.query.CzglRecordQuery;

public interface CzglManage extends Service {

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述： 充值统计记录
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
	 * <li>查询{@link T7022}表 </li>
	 * <li>按照{@code T7022.F03}字段降序排序</li>
	 * <li>
	 * 查询列表：
	 * <ol>
	 * <li>{@link Czgl#totalAmount}对应{@code T7022.F01}</li>
	 * <li>{@link Czgl#charge}对应{@code T7022.F02}</li>
	 * <li>{@link Czgl#updateTime}对应{@code T7022.F03}</li>
	 * </ol>
	 * </li>
	 * </dl>
	 * </dt>
	 * 
	 * @return Recharge 充值实体对象
	 * @throws Throwable
	 */
	public Czgl getRechargeInfo() throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：分页查询充值列表
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
	 * <li>查询 {@link T6033}表  {@link T6010}表  {@link T6011}表</li>
	 * <li>查询条件:
	 * <ol>
	 * <li>{@code T6033.F02=T6010.F01  T6010.F01=T6011.F01 }</li>
	 * <li>如果{@code query != null} 则
	 * <ol>
	 * <li>如果{@link CzglRecordQuery#getSerialNumber()}不为空  则{@code T6033.F07 LIKE }{@link CzglRecordQuery#getSerialNumber()}</li>
	 * <li>如果{@link CzglRecordQuery#getLoginName()}不为空  则{@code T6010.F02 LIKE }{@link CzglRecordQuery#getLoginName()}</li>
	 * <li>如果{@link CzglRecordQuery#getStartRechargeTime()}不为空  则{@code DATE(T6033.F03) >= }{@link CzglRecordQuery#getStartRechargeTime()}</li>
	 * <li>如果{@link CzglRecordQuery#getEndRechargeTime()}不为空  则{@code DATE(T6033.F03) <= }{@link CzglRecordQuery#getEndRechargeTime()}</li>
	 * <li>如果{@link CzglRecordQuery#getPayType()}不为空  则{@code T6033.F06 = }{@link CzglRecordQuery#getPayType()}</li>
	 * <li>如果{@link CzglRecordQuery#getStatus()}不为空  则{@code T6033.F05 = }{@link CzglRecordQuery#getStatus()}</li>
	 * </li>
	 * </ol>
	 * </li>
	 * </ol>
	 * </li>
	 * <li>按照{@code T6033.F03}字段降序排序</li>
	 * <li>
	 * 查询字段列表:
	 * <ol>
	 * <li>{@link CzglRecord#id}对应{@code T6033.F01}</li>
	 * <li>{@link CzglRecord#loginName}对应{@code T6010.F02}</li>
	 * <li>{@link CzglRecord#userName}对应{@code T6011.F06}</li>
	 * <li>{@link CzglRecord#userAmount}对应{@code T6033.F04}</li>
	 * <li>{@link CzglRecord#charge}对应{@code T6033.F09}</li>
	 * <li>{@link CzglRecord#chargeDateTime}对应{@code T6033.F03}</li>
	 * <li>{@link CzglRecord#singleNumber}对应{@code T6033.F07}</li>
	 * <li>{@link CzglRecord#payType}对应{@code T6033.F06}</li>
	 * <li>{@link CzglRecord#rechargeStatus}对应{@code T6033.F05}</li>
	 * 
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>			
	 * 
	 * @param query
	 *            充值查询接口
	 * @param page
	 *            分页对象
	 * @return {@link PagingResult}{@code <}{@link CzglRecord}{@code >} 充值分页集合
	 * @throws Throwable
	 */
	public PagingResult<CzglRecord> getUserRechargeRecordList(
			CzglRecordQuery query, Paging paging) throws Throwable;

}
