package com.dimeng.p2p.escrow.fuyou.entity.console;

import java.math.BigDecimal;


/**
 * 
 * 余额查询实体类
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月31日]
 */
public class BalanceEntity
{
    /**
     * 第三方标识
     */
    public String thirdTag;
    
    /**
     * 富友-账面总余额
     */
    public BigDecimal ct_balance;
    
    /**
     * 富友-可用余额
     */
    public BigDecimal ca_balance;
    
    /**
     * 富友-冻结余额
     */
    public BigDecimal cf_balance;
    
    /**
     * 未转结余额
     */
    public BigDecimal cu_balance;
}
