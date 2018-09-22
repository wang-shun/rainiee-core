package com.dimeng.p2p.repeater.score;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6106;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.repeater.score.entity.MyOrderInfoExt;
import com.dimeng.p2p.repeater.score.entity.ScoreCountExt;
import com.dimeng.p2p.repeater.score.entity.ScoreOrderInfoExt;
import com.dimeng.p2p.repeater.score.query.HarvestAddressQuery;

public abstract interface UserCenterScoreManage extends Service
{
    /**
     * 更新登录签到时间
     * <功能详细描述>
     * @return
     */
    public abstract boolean updateUserSigned()
        throws Throwable;
    
    /**
     * 查询用户可用积分
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract int getUsableScore()
        throws Throwable;
    
    /**
     * 查询总积分、总可用积分、总已用积分、总兑换次数
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract ScoreCountExt getSumScore()
        throws Throwable;
    
    /**
     * 查询积分获取记录
     * @param userId
     * @return
     */
    public abstract PagingResult<T6106> getUserScoreList(Paging page)
        throws Throwable;
    
    /** 查询积分兑换记录
     * <功能详细描述>
     * @param userName
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<ScoreOrderInfoExt> queryUsedUScoreList(Paging page)
        throws Throwable;
    
    /** 查询我的订单
     * <功能详细描述>
     * @param userName
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<MyOrderInfoExt> queryMyOrderList(Paging page)
        throws Throwable;
    
    /**
     * 获取收获地址
     * <功能详细描述>
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<T6355> queryHarvestAddress(Paging page)
        throws Throwable;
    
    /**
     * 获取收获地址个数
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract int getCountAddress()
        throws Throwable;

    /**
     * 新增收获地址
     * <功能详细描述>
     * @param t6355
     * @throws Throwable
     */
    public abstract int addAddress(T6355 t6355)
        throws Throwable;
    
    /**
     * 删除指定的地址
     * <功能详细描述>
     * @param id
     * @throws Throwable
     */
    public abstract int deleteAddress(int id)
        throws Throwable;
    
    /**
     * 修改指定的地址
     * <功能详细描述>
     * @throws Throwable
     */
    public abstract void updateAddress(int id, HarvestAddressQuery query)
        throws Throwable;
    
    /**
     * 根据ID获取地址详情
     * <功能详细描述>
     * @param id
     * @return
     * @throws Throwable
     */
    public abstract T6355 getAddress(int id)
        throws Throwable;
    
    /**
     * 根据ID获取订单详情
     * <功能详细描述>
     * @param id 订单编号
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<MyOrderInfoExt> queryMyOrderById(final String orderId, Paging page)
            throws Throwable;
    
    
    /**
     * 获取默认收获地址
     * <功能详细描述>
     * @param page
     * @return
     * @throws Throwable
     */
    public abstract T6355 queryDefaultAddress()
        throws Throwable;
}
