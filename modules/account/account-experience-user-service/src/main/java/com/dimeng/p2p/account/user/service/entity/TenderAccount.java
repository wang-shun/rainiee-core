package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 投资账户
 */
public class TenderAccount implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 优选理财账户资产
     */
    public BigDecimal yxzc = new BigDecimal(0);
    
    /**
     * 优选理财已赚金额
     */
    public BigDecimal yxyz = new BigDecimal(0);
    
    /**
     * 优选理财平均收益率
     */
    public BigDecimal yxsyl = new BigDecimal(0);
    
    /**
     * 优选理财持有量
     */
    public int yxcyl;
    
    /**
     * 散标账户资产
     */
    public BigDecimal sbzc = new BigDecimal(0);
    
    /**
     * 散标已赚金额
     */
    public BigDecimal sbyz = new BigDecimal(0);
    
    /**
     * 散标平均收益率
     */
    public BigDecimal sbsyl = new BigDecimal(0);
    
    /**
     * 散标持有量
     */
    public int sbcyl;
    
    /**
     * 已挣总金额
     */
    public BigDecimal yzzje = new BigDecimal(0);
    
    /**
     * 已投体验金总额
     */
    public BigDecimal tyjze = BigDecimal.ZERO;
    
    /**
     * 体验金已赚金额
     */
    public BigDecimal tyjyz = BigDecimal.ZERO;
    
    /**
     * 体验金平均收益率
     */
    public BigDecimal tyjsyl = BigDecimal.ZERO;
    
    /**
     * 体验金投资持有量
     */
    public int tyjcyl;
    
    /**
     * 体验金账户资产
     */
    public int tyjzhzc;
    
    /**
     * 体验金待赚金额
     */
    public BigDecimal tyjdz = BigDecimal.ZERO;
}
