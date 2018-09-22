package com.dimeng.p2p.account.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6231_F21;
import com.dimeng.p2p.S62.enums.T6230_F28;
import com.dimeng.p2p.S62.enums.T6231_F27;
import com.dimeng.p2p.S62.enums.T6231_F33;

/**
 *标的列表 
 */
public class Bdlb extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 标类型名称
     */
    public String F01;
    /** 
     * 标ID
     */
    public int F02;

    /** 
     * 借款用户ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 借款标题
     */
    public String F04;

    /** 
     * 借款标类型ID,参考T6211.F01
     */
    public int F05;

    /** 
     * 借款金额
     */
    public BigDecimal F06 = new BigDecimal(0);

    /** 
     * 年化利率
     */
    public BigDecimal F07 = new BigDecimal(0);

    /** 
     * 可投金额
     */
    public BigDecimal F08 = new BigDecimal(0);

    /** 
     * 筹款期限,单位:天
     */
    public int F09;

    /** 
     * 借款周期,单位:月
     */
    public int F10;

    /** 
     * 状态,SQZ:申请中;DSH:待审核;DFB:待发布;YFB:预发布;TBZ:投资中;DFK:待放款;HKZ:还款中;YJQ:已结清;YLB:已流标;YDF:已垫付;
     */
    public T6230_F20 F11;

    /** 
     * 封面图片编码
     */
    public String F12;

    /** 
     * 发布时间,预发布状态有效
     */
    public Timestamp F13;

    /** 
     * 信用等级,参考T5124.F01
     */
    public int F14;

    /** 
     * 等级名称
     */
    public String F15;
    /** 
     * 是否有担保,S:是;F:否;
     */
    public T6230_F11 F16;

    /** 
     * 是否有抵押,S:是;F:否;
     */
    public T6230_F13 F17;
    /** 
     * 是否实地认证,S:是;F:否;
     */
    public T6230_F14 F18;
    /** 
     * 进度
     */
    public double proess;
    /**
     * 是否为按天借款,S:是;F:否
     */
    public T6231_F21 F19;
    /**
     * 借款天数
     */
    public int F20;

    /**
     * 是否新手标
     */
    public T6230_F28 F21;
    /**
     * 是否奖励标
     */
    public T6231_F27 F22;
    /**
     * 奖励标利率
     */
    public BigDecimal F23 = new BigDecimal(0);
    /**
     * 允许投标的终端：'PC_APP','PC','APP'
     */
    public T6231_F33 F24;
}