package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 
 * 不良债权转让记录实体类
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月15日]
 */
public class T6265 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /** 
     * 转让申请ID,参考T6264.F01
     */
    public int F02;
    
    /** 
     * 购买人ID,参考T6110.F01
     */
    public int F03;
    
    /**
     * 借款人ID,参考T6110.F01
     */
    public int F04;
    
    /** 
     * 债权价值
     */
    public BigDecimal F05 = BigDecimal.ZERO;
    
    /** 
     * 认购价格
     */
    public BigDecimal F06 = BigDecimal.ZERO;
    
    /** 
     * 购买时间
     */
    public Timestamp F07;
    
}
