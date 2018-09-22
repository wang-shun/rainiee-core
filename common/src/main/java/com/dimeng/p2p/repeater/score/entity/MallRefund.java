/*
 * 文 件 名:  MallRefund.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月26日
 */
package com.dimeng.p2p.repeater.score.entity;

import java.math.BigDecimal;

import com.dimeng.framework.service.query.PagingResult;

/**
 * <平台退款>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月26日]
 */
public class MallRefund extends ScoreOrderMainRecord
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 6659636966925200664L;
    
    /**
     * 退款金额
     */
    public BigDecimal refundAmount;
    
    /**
     * 购买金额总计
     */
    public BigDecimal buyAllAmount = BigDecimal.ZERO;
    
    /**
     * 退款金额总计
     */
    public BigDecimal refundAllAmount = BigDecimal.ZERO;
    
    public PagingResult<ScoreOrderMainRecord> pagingResult;
}
