package com.dimeng.p2p.modules.bid.pay.service;

import java.math.BigDecimal;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6103;
import com.dimeng.p2p.S63.entities.T6342;

public abstract interface TenderManage extends Service
{
    /**
     * 用户主动投资订单
     * 
     * @param bidId
     *            标ID
     * @param amount
     *            投资金额
     * @param tranPwd
     *            交易密码
     * @return 订单ID
     * @throws Throwable
     */
    public Map<String, String> bid(final int bidId, final BigDecimal amount, String userReward, String tranPwd,
        String myRewardType, String hbRule, String jxqRule, Map<String, String> map)
        throws Throwable;
    
    /**
     * 查询某个标是否用过体验金
     * @param bidId
     * @return
     * @throws Throwable
     */
    T6103 getT6103(int bidId, int accountId)
        throws Throwable;
    
    /**
     * 查询用户活动表
     * @param bidId
     * @return
     * @throws Throwable
     */
    T6342 getT6342(int bidId, int accountId)
        throws Throwable;
    
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;

}
