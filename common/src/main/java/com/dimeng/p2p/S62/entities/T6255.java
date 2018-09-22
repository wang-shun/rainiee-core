package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 
 * 垫付明细
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月23日]
 */
public class T6255 extends AbstractEntity
{
    private static final long serialVersionUID = 1L;
    /**
     * 自增ID
     */
    public int F01;
    /**
     * 垫付id，参考T6253.F01
     */
    public int F02;
    /**
     * 金额
     */
    public BigDecimal F03 = BigDecimal.ZERO;
    /**
     * 收款人（投资人）
     */
    public int F04;
    /**
     * 交易类型
     */
    public int F05;
    
    /**
     * 债权ID，参考T6251.F01
     */
    public int F06;
}
