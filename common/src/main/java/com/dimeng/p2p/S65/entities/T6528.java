/*
 * 文 件 名:  T6528.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年12月28日
 */
package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 平台商城退款订单
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月28日]
 */
public class T6528 extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7519385639889766298L;
    
    /**
     * 订单号,参考T6501.F01
     */
    public int F01;

    /**
     * 收款用户ID,参考T6110.F01
     */
    public int F02;

    /**
     * 退款金额
     */
    public BigDecimal F03 = new BigDecimal(0);

    /**
     * 商城订单明细ID,参考T6359.F01
     */
    public int F04;

}
