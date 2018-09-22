package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 标的线下债权转让关联
 */
public class T6240 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 标的ID
     */
    public int F01;

    /** 
     * 债权人ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 剩余期限
     */
    public int F03;

    /** 
     * 债权价值
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 转让标题
     */
    public String F05;

    /** 
     * 预计收益
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 原始投资金额
     */
    public BigDecimal F07 = BigDecimal.ZERO;

}
