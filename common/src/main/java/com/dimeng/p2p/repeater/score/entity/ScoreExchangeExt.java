package com.dimeng.p2p.repeater.score.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S63.entities.T6352;

public class ScoreExchangeExt extends T6352
{

    private static final long serialVersionUID = 50133751806614060L;
    
    /**
     * 用户名
     */
    public String loginName;
    
    /**
     * 真是姓名
     */
    public String realName;
    
    /**
     * 成交时间开始
     */
    public Timestamp startTime;
    
    /**
     * 成交时间结束
     */
    public Timestamp endTime;
    
    /**
     * 订单编号
     */
    public String orderId;

}
