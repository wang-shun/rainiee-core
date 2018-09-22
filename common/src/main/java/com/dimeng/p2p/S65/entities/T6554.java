package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;

/**
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  公益标订单
 * 修 改 人:  wangming
 * 修改时间:  15-3-11
 */
public class T6554
{
    //订单ID
    public int F01;
    
    //捐款用户ID,参考T6110.F01'
    public int F02;
    
    //标ID,参考T6242.F01
    public int F03;
    
    //投资金额
    public BigDecimal F04;
    
    //投资记录ID
    public int F05;
    
}
