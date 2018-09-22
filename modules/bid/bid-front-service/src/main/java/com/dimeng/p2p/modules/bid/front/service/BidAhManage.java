package com.dimeng.p2p.modules.bid.front.service;

import java.sql.Blob;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S51.enums.T5127_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6240;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdylx;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdysx;
import com.dimeng.p2p.modules.bid.front.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.front.service.entity.IndexStatic;
import com.dimeng.p2p.modules.bid.front.service.entity.Mbxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Sytjsj;
import com.dimeng.p2p.modules.bid.front.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.front.service.entity.Zqzrxx;
import com.dimeng.p2p.modules.bid.front.service.query.BidQuery;
import com.dimeng.p2p.modules.bid.front.service.query.QyAhBidQuery;

/**
 * 标管理
 * 
 */
public interface BidAhManage extends Service {

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据标ID查询标主信息.
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
	public Bdxq get(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：根据ID查询标扩展信息.
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
	public T6231 getExtra(int id) throws Throwable;

	/**
	 * <dt>
	 * <dl>
	 * 描述：标.
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
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Bdlb> search(BidQuery query, Paging paging)
			throws Throwable;
	
	/**
	 * 分页查询所有标
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Bdlb> searchBids(BidQuery query, Paging paging)
			throws Throwable;

	/**
	 * 查询个人标列表
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Bdlb> searchQy(QyAhBidQuery query,
			T6110_F06 f06, Paging paging) throws Throwable;

	/**
	 * 查询所有散标
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Bdlb> searchAll(QyAhBidQuery query, Paging paging)
			throws Throwable;

	/**
	 * 线下债权转让列表
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Bdlb> searchXXZQ(QyAhBidQuery query,
			Paging paging) throws Throwable;

	/**
	 * 查询线下债权转让详情
	 * 
	 * @param bidId
	 * @return
	 * @throws Throwable
	 */
	public abstract T6240 getXXZQ(int bidId) throws Throwable;

	/**
	 * 投资记录
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6250[] getRecord(int id) throws Throwable;

	/**
	 * 查询投资人数
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract int getTbCount(int id) throws Throwable;

	/**
	 * 获取还款记录
	 * 
	 * @param 借款标id
	 * @return
	 */
	public abstract Hkjllb[] getHk(int id) throws Throwable;

	/**
	 * 查询风控信息
	 * 
	 * @param 借款标id
	 * @return
	 */
	public abstract T6237 getFk(int id) throws Throwable;

	/**
	 * 标担保信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract Dbxx getDB(int id) throws Throwable;

	/**
	 * 标抵押列表
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract Bdylx[] getDylb(int id) throws Throwable;

	/**
	 * 根据抵押ID查询抵押属性
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract Bdysx[] getDysx(int id) throws Throwable;

	/**
	 * 标附件列表(非公开)
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6233[] getFjfgk(int id) throws Throwable;

	/**
	 * 标附件列表(公开)
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6232[] getFjgk(int id) throws Throwable;

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
	 * 获取企业投资统计信息.
	 * 
	 * @return {@link Tztjxx}
	 * @throws Throwable
	 */
	public abstract Tztjxx getStatisticsQy() throws Throwable;
	
	/**
	 * 获取个人投资统计信息.
	 * 
	 * @return {@link Tztjxx}
	 * @throws Throwable
	 */
	public abstract Tztjxx getStatisticsGr() throws Throwable;
	
	/**
	 * 获取线下债权投资统计信息.
	 * 
	 * @return {@link Tztjxx}
	 * @throws Throwable
	 */
	public abstract Tztjxx getStatisticsXxzq() throws Throwable;

	/**
	 * 标的类型列表
	 * 
	 * @return {@link Tztjxx}
	 * @throws Throwable
	 */
	public abstract T6211[] getBidType() throws Throwable;

	/**
	 * 信用等级列表
	 * 
	 * @return {@link Tztjxx}
	 * @throws Throwable
	 */
	public abstract T5124[] getLevel() throws Throwable;

	/**
	 * 查询满标的待还金额
	 * 
	 * @param loanId
	 * @return
	 * @throws Throwable
	 */
	public abstract Mbxx getMbxx(int loanId) throws Throwable;

	/**
	 * 根据附件ID查询附件内容
	 */
	public abstract Blob getAttachment(int id) throws Throwable;

	/**
	 * 查询用户的借款列表
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Bdlb> searchCredit(int userId, Paging paging)
			throws Throwable;

	/**
	 * 查询预发布的标信息
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract Bdlb getNewBid() throws Throwable;

	/**
	 * 首页统计数据
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract Sytjsj getSytj() throws Throwable;

	/**
	 * 查询首页统计
	 */
	public abstract IndexStatic getIndexStatic() throws Throwable;
	/**
	 * 获取线下债权转让
	 * @param loanId
	 * @return
	 * @throws Throwable
	 */
	public abstract T6240 getT6240(int loanId)throws Throwable;
	
	/**
	 * 获取标附件类型列表
	 * @param loanId
	 * @return
	 * @throws Throwable
	 */
	public abstract T6212[] getT6212(int loanId,boolean b)throws Throwable;
	
	/**
	 * 根据用户的ID得到用户的投资等级
	 * @param userID
	 * @return
	 * @throws Throwable
	 */
	public abstract T5127_F03 getTzdj(int userID) throws Throwable;
	
	
	/**
	 * 查询担保产品
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Bdlb> searchDb(QyAhBidQuery query,
			T6110_F06 f06, Paging paging) throws Throwable;
	
	/**
	 * 查询融资产品
	 * 
	 * @param query
	 * @param paging
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<Bdlb> searchRz(QyAhBidQuery query,
			T6110_F06 f06, Paging paging) throws Throwable;
	
	
}
