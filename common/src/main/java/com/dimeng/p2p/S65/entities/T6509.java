package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 线下充值订单
 */
public class T6509 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 订单ID,参考T6501.F01
     */
    public int F01;

    /** 
     * 充值用户ID,参考T6501.F01
     */
    public int F02;

    /** 
     * 充值金额
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 支付公司代号
     */
    public int F04;

    /** 
     * 流水单号,充值成功时记录
     */
    public String F05;
    
    /** 
     * 线下充值申请id
     */
    public int F06;

}
