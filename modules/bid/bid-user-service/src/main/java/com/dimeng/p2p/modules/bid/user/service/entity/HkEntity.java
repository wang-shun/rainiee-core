package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6230_Ext;

public class HkEntity extends T6230
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 670947648821982831L;
    
    /**
     * 还款总金额
     */
    public BigDecimal hkTotle;
    
    /**
     * 当期应还金额
     */
    public BigDecimal dqyhje;
    
    /**
     * 是否逾期
     */
    public boolean isYuqi;
    
    /**
     * 还款信息
     */
    public RepayInfo repayInfo;
    
    /**
     * 提前还款信息
     */
    public ForwardRepayInfo forwardRepayInfo;
    
    /**
     * 未还期数
     */
    public int leftNum;
    /**
     * 总期数
     */
    public int totalNum;

    /**
     * 自动还款授权
     */
    public T6230_Ext ext;
}
