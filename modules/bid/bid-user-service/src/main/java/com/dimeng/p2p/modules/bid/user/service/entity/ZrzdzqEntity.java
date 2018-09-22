package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6260_F07;

/**
 * 转让中的债权
 *
 */
public class ZrzdzqEntity extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 债权ID,参考T6251.F01
     */
    public int F02;

    /** 
     * 转让价格
     */
    public BigDecimal F03 = new BigDecimal(0);

    /** 
     * 已转债权
     */
    public BigDecimal F04 = new BigDecimal(0);

    /** 
     * 创建时间
     */
    public Timestamp F05;

    /** 
     * 结束时间
     */
    public Timestamp F06;

    /** 
     * 状态,DSH:待审核;ZRZ:转让中;YJS:已结束;
     */
    public T6260_F07 F07;

    /** 
     * 转让手续费率
     */
    public BigDecimal F08 = new BigDecimal(0);

    /** 
     * 债权编码
     */
    public String F09;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F10;

    /** 
     * 债权人ID,参考T6110.F01
     */
    public int F11;

    /** 
     * 购买价格
     */
    public BigDecimal F12 = new BigDecimal(0);

    /** 
     * 原始债权金额
     */
    public BigDecimal F13 = new BigDecimal(0);

    /** 
     * 持有债权金额
     */
    public BigDecimal F14 = new BigDecimal(0);

    /** 
     * 是否正在转让,S:是;F:否;
     */
    public T6251_F08 F15;

    /** 
     * 创建日期
     */
    public Date F16;

    /** 
     * 起息日期
     */
    public Date F17;

    /** 
     * 投资记录ID,参考T6250.F01
     */
    public int F18;

    /**
     * 债权年化利率
     */
    public BigDecimal F19;

    /**
     * 债权总期数
     */
    public int F20;

    /**
     * 债权剩余期数
     */
    public int F21;

    /**
     * 状态,待审核;转让中;已结束;
     */
    public String F22;

    /**
     * 债权年化利率
     */
    public String F23;

}
