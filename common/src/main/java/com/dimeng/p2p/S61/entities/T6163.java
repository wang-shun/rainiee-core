package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 企业财务状况
 */
public class T6163 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 年份
     */
    public int F02;

    /** 
     * 主营收入(万元)
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 毛利率(万元)
     */
    public BigDecimal F04 = BigDecimal.ZERO;

    /** 
     * 净利润(万元)
     */
    public BigDecimal F05 = BigDecimal.ZERO;

    /** 
     * 总资产(万元)
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 净资产(万元)
     */
    public BigDecimal F07 = BigDecimal.ZERO;

    /** 
     * 备注
     */
    public String F08;

}
