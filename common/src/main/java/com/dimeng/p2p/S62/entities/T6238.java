package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 标的费率
 */
public class T6238 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 标的ID,参考T6230.F01
     */
    public int F01;

    /** 
     * 成交服务费率
     */
    public BigDecimal F02 = BigDecimal.ZERO;

    /** 
     * 投资管理费率
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 逾期罚息利率
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    /** 
     * 担保费率
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 月还款本息
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 月担保费
     */
    public BigDecimal F07 = BigDecimal.ZERO;
    /** 
     * 月管理费
     */
    public BigDecimal F08 = BigDecimal.ZERO;

    /** 
     * 月应付总金额
     */
    public BigDecimal F09 = BigDecimal.ZERO;
    
    /** 
     * 投票奖励利率
     */
    public BigDecimal F10 = BigDecimal.ZERO;


}
