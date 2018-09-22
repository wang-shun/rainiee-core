package com.dimeng.p2p.S65.entities;

import com.dimeng.framework.service.AbstractEntity;
import java.math.BigDecimal;
/**
 * 
 * 商品购买订单
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月25日]
 */
public class T6555 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 订单ID
     */
    public int F01;

    /** 
     * 购买用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 收货地址ID,参考T6355.F01
     */
    public int F03;

    /** 
     * 购买金额
     */
    public BigDecimal F04 = new BigDecimal(0);

    /** 
     * 商品订单ID,参考T6352.F01,购买成功时记录
     */
    public int F05;

}