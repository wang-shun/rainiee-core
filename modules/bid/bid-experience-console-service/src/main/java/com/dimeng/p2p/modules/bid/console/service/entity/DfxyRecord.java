package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 垫付协议
 * @author beiweiyuan
 *
 */
public class DfxyRecord extends AbstractEntity
{
    /**
     * 
     */
    private static final long serialVersionUID = -4920136745439525767L;
    
    /**
     * 标的编号 T6230.F01
     */
    public int loanId;
    
    /**
     * 垫付记录编号 T6253.F01
     */
    public int advanceId;
    
    /**
     * 借款标题
     */
    public String loanTitle;
    
    /**
     * 借款（人）名称
     */
    public String loanName;
    
    /**
     * 借款账号id
     */
    public int loanAccnoutId;
    
    /**
     * 借款账号
     */
    public String loanAccnout;
    
    /**
     * 担保方名称（垫付方名称）
     */
    public String advanceName;
    
    /**
     * 担保账号Id
     */
    public int advanceAccountId;
    
    /**
     * 担保账号（垫付方账号）
     */
    public String advanceAccount;
    
    /**
     * 垫付金额
     */
    public BigDecimal advanceSum;
    
    /**
     * 垫付时间
     */
    public Timestamp advanceTime;
}
