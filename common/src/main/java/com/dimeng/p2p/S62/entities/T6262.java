package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6262_F11;

import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 债权转让记录
 */
public class T6262 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 转让申请ID,参考T6261.F01
     */
    public int F02;

    /** 
     * 购买人ID,参考T6110.F01
     */
    public int F03;

    /** 
     * 购买债权
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 受让价格
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 转让手续费
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 购买时间
     */
    public Timestamp F07;

    /** 
     * 转入盈亏
     */
    public BigDecimal F08 = BigDecimal.ZERO;

    /** 
     * 转出盈亏
     */
    public BigDecimal F09 = BigDecimal.ZERO;

    /**
     * 债权剩余期数
     */
    public int F10;
    
    /**
	 * 来源,PC:PC;APP:APP;WEIXIN:微信;
	 */
    public T6262_F11 F11;
    
}
