package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 用户车产信息
 */
public class T6113 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 车产ID,自增
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 汽车品牌
     */
    public String F03;

    /** 
     * 车牌号码
     */
    public String F04;

    /** 
     * 购车年份
     */
    public int F05;

    /** 
     * 购买价格
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 评估价格
     */
    public BigDecimal F07 = BigDecimal.ZERO;

}
