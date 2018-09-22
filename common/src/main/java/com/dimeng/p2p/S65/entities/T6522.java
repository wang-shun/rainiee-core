package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 体验金投资取消订单
 */
public class T6522 extends AbstractEntity{

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
     * 投资记录ID,参考T6286.F01
     */
    public int F03;

}
