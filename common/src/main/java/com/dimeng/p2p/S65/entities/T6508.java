package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 投资取消订单
 */
public class T6508 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 订单ID
     */
    public int F01;

    /** 
     * 投资用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 投资记录ID,参考T6250.F01
     */
    public int F03;
    
    /** 
     * 流标原因
     */
    public String F04;

}
