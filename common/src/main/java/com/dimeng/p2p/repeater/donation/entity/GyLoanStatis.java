package com.dimeng.p2p.repeater.donation.entity;

import com.dimeng.framework.service.AbstractEntity;

import java.math.BigDecimal;

/**
 * <公益统计>
 * <功能详细描述>
 * 
 * @author  liuguangwen
 * @version  [版本号, 2015年3月12日]
 */
public class GyLoanStatis  extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 发布总金额,只有发布审核成功,是捐款中,已捐助状态下的公益标
     */
    public BigDecimal totalAmount=BigDecimal.ZERO;
    
    /**
     * 总捐款人数,从捐款记录表统计
     */
    public int totalNum;
    
    /**
     * 捐款笔数,从捐款记录表统计
     */
    public int donationsNum;
    
    /**
     * 捐款总金额,从捐款记录表统计
     */
    public BigDecimal donationsAmount = BigDecimal.ZERO;
    
    /**
     * 捐款的公益标数量,统计用户投了几个公益标
     */
    public int totalDonBs;
}
