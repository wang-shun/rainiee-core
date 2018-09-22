package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 
 * 不良债权转让还款明细实体类
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月15日]
 */
public class T6266 extends AbstractEntity
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 自增ID
     */
    public int F01;
    
    /**
     * 转让id，参考T6265.F01
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
