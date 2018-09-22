/*
 * 文 件 名:  T6525.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月13日
 */
package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [v3.1.2, 2015年10月13日]
 */
public class T6525 extends AbstractEntity
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 订单ID,参考T6501.F01
     */
    public int F01;
    
    /**
     * 投资用户ID,参考T6110.F01
     */
    public int F02;
    
    /**
     * 标ID,参考T6230.F01
     */
    public int F03;
    
    /**
     * 返还利息期号
     */
    public int F04;
    
    /**
     * 返还利息
     */
    public BigDecimal F05;
    
    /**
     * 科目类型ID,参考T5122.F01
     */
    public int F06;
    
    /**
     * 返还利息平台ID
     */
    public int F07;
    
    /**
     * 加息券返还ID，参考T6289.F01
     */
    public int F08;
    
    /**
     * 标的放款订单ID，参考T6505.F01
     */
    public int F09;
}
