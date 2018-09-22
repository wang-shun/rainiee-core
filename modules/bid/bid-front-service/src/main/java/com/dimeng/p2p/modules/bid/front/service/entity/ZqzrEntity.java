package com.dimeng.p2p.modules.bid.front.service.entity;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6251_F08;
import com.dimeng.p2p.S62.enums.T6260_F07;

public class ZqzrEntity extends AbstractEntity{
	
	private static final long serialVersionUID = 1L;

    /** 
     * 债权编码
     */
    public String F01;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 债权人ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 购买价格
     */
    public BigDecimal F04 = new BigDecimal(0);

    /** 
     * 原始债权金额
     */
    public BigDecimal F05 = new BigDecimal(0);

    /** 
     * 持有债权金额
     */
    public BigDecimal F06 = new BigDecimal(0);

    /** 
     * 是否正在转让,S:是;F:否;
     */
    public T6251_F08 F07;

    /** 
     * 创建日期
     */
    public Date F08;

    /** 
     * 起息日期
     */
    public Date F09;

    /** 
     * 自增ID
     */
    public int F10;

    /** 
     * 债权ID,参考T6251.F01
     */
    public int F11;

    /** 
     * 转让价格
     */
    public BigDecimal F12 = new BigDecimal(0);

    /** 
     * 已转债权
     */
    public BigDecimal F13 = new BigDecimal(0);

    /** 
     * 创建时间
     */
    public Timestamp F14;

    /** 
     * 结束时间
     */
    public Timestamp F15;

    /** 
     * 状态,DSH:待审核;ZRZ:转让中;YJS:已结束;
     */
    public T6260_F07 F16;

    /** 
     * 转让手续费率
     */
    public BigDecimal F17 = new BigDecimal(0);
}
