package com.dimeng.p2p.S63.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6335_F02;
import java.math.BigDecimal;

/** 
 * 券使用规则信息表
 */
public class T6335 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增主键ID
     */
    public int F01;

    /** 
     * 使用场景,TZ:投资;
     */
    public T6335_F02 F02;

    /** 
     * 单笔投资金额下限
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 单笔投资基数金额
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 单笔投资使用券金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 单笔投资使用券金额上限
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 规则说明
     */
    public String F07;

}
