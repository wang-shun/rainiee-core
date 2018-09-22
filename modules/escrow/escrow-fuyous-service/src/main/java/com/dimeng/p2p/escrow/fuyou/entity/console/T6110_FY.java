package com.dimeng.p2p.escrow.fuyou.entity.console;

import java.math.BigDecimal;

import com.dimeng.p2p.S61.entities.T6110;

/**
 * 用户余额
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月31日]
 */
public class T6110_FY extends T6110
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 第三方标识
     */
    public String thirdTag;
    
    /**
     * 平台-账面总余额
     */
    public BigDecimal pt_balance;
    
    /**
     * 平台-可用余额
     */
    public BigDecimal pa_balance;
    
    /**
     * 平台-冻结余额
     */
    public BigDecimal pf_balance;
    
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
