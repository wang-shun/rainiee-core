package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 转账订单
 */
public class T6517 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 主键id
     */
    public int F01;

    /** 
     * 金额
     */
    public BigDecimal F02 = BigDecimal.ZERO;

    /** 
     * 出账账号id
     */
    public int F03;

    /** 
     * 入账账户id
     */
    public int F04;

    /** 
     * 描述
     */
    public String F05;
    
    /**
     * 转账业务类型
     */
    public int F06;
}
