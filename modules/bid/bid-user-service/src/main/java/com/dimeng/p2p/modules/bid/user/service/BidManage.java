package com.dimeng.p2p.modules.bid.user.service;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S51.entities.T5124;
import com.dimeng.p2p.S62.entities.T6211;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.S62.entities.T6240;
import com.dimeng.p2p.S62.entities.T6250;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdlb;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdylx;
import com.dimeng.p2p.modules.bid.user.service.entity.Bdysx;
import com.dimeng.p2p.modules.bid.user.service.entity.Dbxx;
import com.dimeng.p2p.modules.bid.user.service.entity.Hkjllb;
import com.dimeng.p2p.modules.bid.user.service.entity.Mbxx;
import com.dimeng.p2p.modules.bid.user.service.entity.TzjlEntity;
import com.dimeng.p2p.modules.bid.user.service.entity.Tztjxx;
import com.dimeng.p2p.modules.bid.user.service.query.BidQuery;

/**
 * 标管理
 * 
 */
public interface BidManage extends Service {

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
	 * 根据标ID查询逾期金额
	 * 
	 * @param loanId
	 * @return
	 * @throws Throwable
	 */
	public BigDecimal getYqje(int loanId) throws Throwable;

	/**
	 * 添加垫付订单
	 */
	public abstract List<Integer> addOrder(int loanId) throws Throwable;
	
	/**
	 * 更新订单状态为待确认
	 */
	public abstract void updateOrderStatus(List<Integer> orderId)
			throws Throwable;
	
	/**
	 * 根据附件ID查询附件内容
	 */
	public abstract Blob getAttachment(int id) throws Throwable;
	
	/**
	 * 分页查询投资记录
	 * @param query 查询条件
	 * @param paging 分页条件
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<TzjlEntity> searchTzjl(TzjlEntity query,Paging paging) throws Throwable;
	
	/**
	 * 查询满标的待还金额
	 * 
	 * @param loanId
	 * @return
	 * @throws Throwable
	 */
	public abstract Mbxx getMbxx(int loanId) throws Throwable;
	
	/**
	 * 查询标是否有相关文件
	 * 
	 * @param loanId
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isXgwj(int loanId) throws Throwable;
	
	/**
	 * 投资记录
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6250[] getRecord(int id) throws Throwable;
	
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
	 * 查询线下债权转让详情
	 * 
	 * @param bidId
	 * @return
	 * @throws Throwable
	 */
	public abstract T6240 getXXZQ(int bidId) throws Throwable;
	
	/**
	 * 标担保信息
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract Dbxx getDB(int id) throws Throwable;
	
	/**
	 * 查询风控信息
	 * 
	 * @param 借款标id
	 * @return
	 */
	public abstract T6237 getFk(int id) throws Throwable;
	
	/**
	 * 获取个人投资统计信息.
	 * 
	 * @return {@link Tztjxx}
	 * @throws Throwable
	 */
	public abstract Tztjxx getStatisticsGr() throws Throwable;
	
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
	
	public abstract PagingResult<Bdlb> search(BidQuery query, Paging paging)
			throws Throwable;
	
	/**
	 * 获取还款记录
	 * 
	 * @param 借款标id
	 * @return
	 */
	public abstract Hkjllb[] getHk(int id) throws Throwable;
	
	/**
	 * 获取企业投资统计信息.
	 * 
	 * @return {@link Tztjxx}
	 * @throws Throwable
	 */
	public abstract Tztjxx getStatisticsQy() throws Throwable;
	
	/**
	 * 标附件列表(公开)
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6232[] getFjgk(int id) throws Throwable;
	
	/**
	 * 标附件列表(非公开)
	 * 
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract T6233[] getFjfgk(int id) throws Throwable;
	
	/**
	 * 获取标附件类型列表
	 * 
	 * @param loanId
	 * @return
	 * @throws Throwable
	 */
	public abstract T6212[] getT6212(int loanId, boolean b) throws Throwable;
	
	/**
	 * 获取线下债权投资统计信息.
	 * 
	 * @return {@link Tztjxx}
	 * @throws Throwable
	 */
	public abstract Tztjxx getStatisticsXxzq() throws Throwable;
	
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
     * 修改标的状态为已作废
     */
    public abstract int updateBidStatus(int loanId)
        throws Throwable;
    
    
    public Bdxq getT6230(int id) throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;
		
		 /**
     * 添加平台垫付订单
     * @param loanId
     * @return
     * @throws Throwable
     */
    public List<Integer> addPtdfOrder(int loanId) throws Throwable;
    
    /**
     * 判断标的是否存在逾期
     * @param loanId
     * @return
     * @throws Throwable
     */
    public abstract boolean isYQ(int loanId) throws Throwable;
}


