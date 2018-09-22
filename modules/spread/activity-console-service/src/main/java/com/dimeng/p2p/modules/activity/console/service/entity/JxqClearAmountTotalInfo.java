/*
 * 文 件 名:  JxqClearAmountTotalInfo.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月10日
 */
package com.dimeng.p2p.modules.activity.console.service.entity;

import java.math.BigDecimal;

/**
 * 加息券结算统计
 * 待付加息券奖励金额总计，已付加息券奖励金额总计
 * 
 * @author  xiaoqi
 * @version  [v3.1.2, 2015年10月10日]
 */
public class JxqClearAmountTotalInfo
{
    public BigDecimal paidAmount;
    
    public BigDecimal unPayAmount;
    
    public BigDecimal totalAmount;
}
