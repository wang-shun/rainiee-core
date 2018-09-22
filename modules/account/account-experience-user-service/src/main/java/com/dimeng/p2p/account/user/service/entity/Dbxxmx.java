package com.dimeng.p2p.account.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.enums.T6230_F12;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F19;

/**
 * 担保信息明细
 */
public class Dbxxmx extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 借款标ID
     */
    public int jkbId;
    
    /**
     * 借款用户ID,参考T6110.F01
     */
    public int F01;
    
    /**
     * 借款标题
     */
    public String F02;
    
    /**
     * 借款金额
     */
    public BigDecimal F03 = new BigDecimal(0);
    
    /**
     * 年化利率
     */
    public BigDecimal F04 = new BigDecimal(0);
    
    /**
     * 借款周期,单位:月
     */
    public int F05;
    
    /**
     * 状态,SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投资中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:
     * 已流标;YDF:已垫付;YZR:已转让;
     */
    public T6230_F20 F06;
    
    /**
     * 待还金额
     */
    public BigDecimal dhbj;
    
    /**
     * S:是(逾期);F:否;YZYQ:严重逾期
     */
    public T6231_F19 F19;
    
    /**
     * 担保方式
     */
    public T6230_F12 F12;
    
    /**
     * 标扩展信息
     */
    public T6231 F13;
    
    /** 
     * 可投金额
     */
    public BigDecimal F14 = BigDecimal.ZERO;
    
    /**
     * 借款总期数
     */
    public int F15;
    
    /**
     * 借款剩余期数
     */
    public int F16;
    
    /**
     * 状态
     */
    public String state;
    
    /**
     * 是否逾期
     */
    public String isYQ;
    
    /**
     * 用户名
     */
    public String userName;
    
    /**
     * 担保方式
     */
    public String dbfs;
    
}
