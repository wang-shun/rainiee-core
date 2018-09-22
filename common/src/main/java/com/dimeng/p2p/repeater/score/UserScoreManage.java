package com.dimeng.p2p.repeater.score;

import java.io.OutputStream;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6105;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.repeater.score.entity.ScoreCountExt;
import com.dimeng.p2p.repeater.score.entity.ScoreExchangeExt;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt;
import com.dimeng.p2p.repeater.score.entity.UsedScoreExchangeExt;
import com.dimeng.p2p.repeater.score.entity.UserScoreAccountExt;
import com.dimeng.p2p.repeater.score.entity.UserScoreExt;

public abstract interface UserScoreManage extends Service
{
    /**
     * 查询积分账户列表
     * @param userName
     * @param page
     * @return
     */
    public abstract PagingResult<T6105> queryUserScoreAccountList(UserScoreAccountExt userScoreAccountExt, Paging page)
        throws Throwable;
    
    /**
     * 导出个人信息列表
     * <功能详细描述>
     * @param users
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public void exportScoreStatisticsList(T6105[] t6105S, OutputStream outputStream, String charset)
        throws Throwable;

    /** 查询我的积分兑换记录
     * <功能详细描述>
     * @param userName
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<ScoreOrderInfoExt> queryUsedUScoreList(int userId, Paging page)
        throws Throwable;
    
    /**
     * 查询我的积分获取记录
     * @param userId
     * @return
     */
    public abstract PagingResult<T6106> getUserScoreList(int userId, Paging page)
        throws Throwable;
    
    /**
     * 查询积分获取记录列表
     * <功能详细描述>
     * @param query
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T6106> queryScoreList(UserScoreExt userScoreExt, Paging page)
        throws Throwable;
    
    /**
     * 导出积分获取记录列表
     * <功能详细描述>
     * @param t6106S
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public void exportScoreGetList(T6106[] t6106S, OutputStream outputStream, String charset)
        throws Throwable;

    /**
     * 查询积分获取总计
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract int queryGetSumScore(UserScoreExt userScoreExt)
        throws Throwable;

    /**
     * 查询积分兑换记录列表
     * <功能详细描述>
     * @param scoreExchangeExt
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<ScoreOrderInfoExt> queryScoreExchangeList(ScoreExchangeExt scoreExchangeExt,
        Paging page)
        throws Throwable;
    
    /**
     * 导出积分兑换记录列表
     * <功能详细描述>
     * @param t6105S
     * @param outputStream
     * @param charset
     * @throws Throwable
     */
    public void exportScoreExchangeList(ScoreOrderInfoExt[] scoreOrderInfoExtS, OutputStream outputStream,
        String charset)
        throws Throwable;

    /**
     * 查询积分兑换商品数量总计、积分总计
     * <功能详细描述>
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract UsedScoreExchangeExt getSumScoreExchange(ScoreExchangeExt scoreExchangeExt)
        throws Throwable;
    
    /**
     * 查询总积分、总可用积分、总已用积分、总兑换次数
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract ScoreCountExt getSumScore(UserScoreAccountExt userScoreAccountExt)
        throws Throwable;
    
    /**
     *  查询我的积分获取总计
     * <功能详细描述>
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract int getSumScore(int userId)
        throws Throwable;
    
    /**
     * 查询我的积分兑换商品数量总计、积分总计
     * <功能详细描述>
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract UsedScoreExchangeExt getSumScoreExchange(int userId)
        throws Throwable;
}
