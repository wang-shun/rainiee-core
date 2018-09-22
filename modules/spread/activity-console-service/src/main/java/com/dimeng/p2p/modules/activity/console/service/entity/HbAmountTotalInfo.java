/*
 * 文 件 名:  HbAmountTotalInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  红包[未使用,已使用]金额统计
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月9日
 */
package com.dimeng.p2p.modules.activity.console.service.entity;

import java.math.BigDecimal;

/**
 * 红包[未使用,已使用]金额统计
 * English: RedPacket[红包] ^_^
 * @author  xiaoqi
 * @version  [v3.1.2, 2015年10月9日]
 */
public class HbAmountTotalInfo
{
    /**
     * 未使用红包金额总计
     */
    public BigDecimal unUsedAmount;
    
    /**
     * 已使用红包金额总计
     */
    public BigDecimal usedAmount;
    
    /**
     * 已发放的红包金额合计
     */
    public BigDecimal totalAmount;
}
