package com.dimeng.p2p.modules.bid.console.service;

import java.sql.SQLException;
import java.sql.Timestamp;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6234;
import com.dimeng.p2p.S62.entities.T6235;
import com.dimeng.p2p.S62.entities.T6236;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6238;
import com.dimeng.p2p.S62.entities.T6240;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.modules.bid.console.service.entity.Bid;
import com.dimeng.p2p.modules.bid.console.service.entity.BidDyw;
import com.dimeng.p2p.modules.bid.console.service.entity.BidDywsx;
import com.dimeng.p2p.modules.bid.console.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.console.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.console.service.entity.TbRecord;
import com.dimeng.p2p.modules.bid.console.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.console.service.entity.Zqzrxx;
import com.dimeng.p2p.modules.bid.console.service.query.LoanQuery;

/**
 * 线下债权转让管理
 */
public abstract interface OfflineManage extends Service {
	/**
	 * 查询区域名称
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public String getRegion(int id) throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：线下债权转让管理.
	 * </dl>
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Bid> search(LoanQuery query, Paging paging)
			throws Throwable;

	/**
	 * <dl>
	 * 描述：根据ID查询标扩展信息.
	 * </dl>
	 * 
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public T6231 getExtra(int id) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：查询标的详细.
	 * </dl>
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6230 get(int id) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：查询标的费率.
	 * </dl>
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6238 getBidFl(int id) throws Throwable;

	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：查询线下债权转让信息
	 * </dl>
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6240 getXxzq(int id) throws Throwable;

	/**
	 * 投资记录
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract TbRecord[] getRecord(int id) throws Throwable;

	/**
	 * 获取还款记录
	 * 
	 * @param 借款标id
	 * @return
	 */
	public abstract Hkjllb[] getHk(int id) throws Throwable;

	/**
	 * 标债权信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6251[] getZqxx(int id) throws Throwable;

	/**
	 * 线上债权转让信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract Zqzrxx[] getZqzrxx(int id) throws Throwable;

	/**
	 * 获取投资统计信息.
	 * 
	 * @return {@link InvestStatistics}
	 * @throws Throwable
	 */
	public abstract Tztjxx getStatistics() throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：新建标.
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
	 * @param t6230
	 * @param t6231
	 * @return
	 * @throws Throwable
	 */
	public int add(T6230 t6230, T6231 t6231, T6238 t6238, T6240 t6240)
			throws Throwable;

	/**
	 * * <dt>
	 * <dl>
	 * 描述：提交审核.
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
	 * @throws Throwable
	 */
	public abstract void submit(int loanId) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：修改标.
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
	 * @param t6230
	 * @param t6231
	 * @return
	 * @throws Throwable
	 */
	public void update(T6230 t6230, T6231 t6231, T6238 t6238, T6240 t6240)
			throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：查询标的类型.
	 * </dl>
	 * 
	 * 
	 * @return
	 * @throws Throwable
	 */
	public T6211[] getBidType() throws Throwable;

	/**
	 * 
	 * <dl>
	 * 描述：根据标的ID查询是什么类型的标
	 * </dl>
	 * 
	 * 
	 * @return
	 * @throws Throwable
	 */
	public T6230 getBidType(int loanId) throws Throwable;

	/**
	 * 查询抵押物列表
	 */
	public T6234[] searchBidDyw(int loanId) throws Throwable;

	/**
	 * 根据抵押物ID查询抵押物属性
	 */
	public BidDywsx[] searchBidDywsx(int id) throws Throwable;

	/**
	 * 根据标抵押物ID查询标的抵押物列表
	 * 
	 * @throws Throwable
	 */
	public T6234 getBidDyw(int id) throws Throwable;

	/**
	 * 添加抵押物
	 */
	public void addBidDyw(BidDyw bidDyw) throws Throwable;

	/**
	 * 修改抵押物
	 */
	public void updateBidDyw(BidDyw bidDyw) throws Throwable;

	/**
	 * 查询所有标的担保方列表
	 */
	public Dbxx[] searchBidDb(int loanId) throws Throwable;

	/**
	 * 根据担保方列表Id查询标的担保信息
	 */
	public Dbxx getBidDb(int id) throws Throwable;

	/**
	 * 根据标ID查询标的风险信息
	 */
	public T6237 getBidFx(int loanId) throws Throwable;

	/**
	 * 添加标的担保信息
	 */
	public abstract void addGuarantee(T6236 t6236) throws Throwable;

	/**
	 * 修改标的担保信息
	 */
	public abstract void updateGuarantee(T6236 t6236) throws Throwable;

	/**
	 * 添加标的风险信息
	 */
	public abstract void addFx(T6237 t6237) throws Throwable;

	/**
	 * 标的审核通过
	 */
	public abstract void through(int loanId) throws Throwable;

	/**
	 * 标的审核不通过
	 */
	public abstract void notThrough(int loanId, String des) throws Throwable;

	/**
	 * 标的发布
	 */
	public abstract void release(int loanId) throws Throwable;

	/**
	 * 标的预发布
	 */
	public abstract void preRelease(int loanId, Timestamp releaseTime)
			throws Throwable;

	/**
	 * 删除抵押物
	 */
	public abstract void delDyw(int id) throws Throwable;

	/**
	 * 删除担保信息
	 */
	public abstract void delDbxx(int id) throws Throwable;

	/**
	 * 修改标的状态为已作废
	 */
	public abstract void updateBidStatus(int loanId) throws Throwable;
	
	/**
	 * 修改抵押物信息
	 */
	public void updateBidDywDate(T6235 t6235) throws Throwable;
	
	/**
	 * 查询抵押物信息
	 */
	public T6235 finBidDywDate(int loanId) throws Throwable;
}
