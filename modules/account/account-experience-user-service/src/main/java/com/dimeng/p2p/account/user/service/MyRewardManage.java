package com.dimeng.p2p.account.user.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S63.entities.T6342;
import com.dimeng.p2p.account.user.service.entity.MyRewardRecod;

/**
 * 我的奖励接口
 * 包括 我的红包、我的加息券
 */
public interface MyRewardManage extends Service
{

    /**
     * 我的奖励列表
     * 
     * @return
     * @throws Throwable
     */
    PagingResult<MyRewardRecod> searchMyReward(Map<String, Object> params, Paging paging)
        throws Throwable;
    
    /**
     * 统计加息券数量
     * 
     * @return
     * @throws Throwable
     */
    int getJxqCount(Map<String, Object> params) throws Throwable;

    /**
     * 统计已使用的加息券数量
     *
     * @return
     * @throws Throwable
     */
    int getJxqUsedCount(Map<String, Object> params) throws Throwable;
    
    /**
     * 统计红包金额
     * 
     * @return
     * @throws Throwable
     */
    BigDecimal getHbAmount(Map<String, Object> params) throws Throwable;
    
    /**
     * 查找我的奖励
     * @return
     * @throws Throwable
     */
    List<MyRewardRecod> getMyRewardRecodList(Map<String, Object> params);
    
    /**
     * 查询用户活动表
     * @param bidId
     * @return
     * @throws Throwable
     */
    T6342 getT6342(int bidId) throws Throwable;

    /**
     * 查询用户活动表
     * @param bidId
     * @param bidId
     * @return
     * @throws Throwable
     */
    boolean IsUsedReward(int bidId,String rewardType) throws Throwable;
    
    /**
     * 统计加息券金额
     * 
     * @return
     * @throws Throwable
     */
    MyRewardRecod getJxqAmount() throws Throwable;
}
