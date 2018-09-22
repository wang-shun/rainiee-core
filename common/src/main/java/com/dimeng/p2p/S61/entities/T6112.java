package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;

/** 
 * 用户房产信息
 */
public class T6112 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户房产ID,自增
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 小区名称
     */
    public String F03;

    /** 
     * 建筑面积(m2)
     */
    public float F04;

    /** 
     * 使用年限,单位:年
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

    /** 
     * 区划ID,参考T5119.F01
     */
    public int F08;

    /** 
     * 地址
     */
    public String F09;

    /** 
     * 房产证编号
     */
    public String F10;

    /** 
     * 参考价格
     */
    public BigDecimal F11 = BigDecimal.ZERO;

}
