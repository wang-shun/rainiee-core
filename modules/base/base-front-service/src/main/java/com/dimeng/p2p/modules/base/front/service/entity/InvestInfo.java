package com.dimeng.p2p.modules.base.front.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 
 * <首页-投资信息>
 * 投资排行，TA发布了，TA投资了
 */
public class InvestInfo implements Serializable
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5921486919518015511L;
    
    //投资时间或发标时间
    public Timestamp bidTime;
    
    //投资金额或借款金额
    public Double amount;
    
    //与当前时间间隔多少分钟
    public String intervalTime;
    
    //投资人或借款人
    public String loginName;
    
    //标的名称
    public String biddingTitle;
    
}
