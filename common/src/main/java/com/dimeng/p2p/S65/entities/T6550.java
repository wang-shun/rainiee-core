package com.dimeng.p2p.S65.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 订单异常日志
 */
public class T6550 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 订单ID,参考T6501.F01
     */
    public int F02;

    /** 
     * 异常信息
     */
    public String F03;

    /** 
     * 发生时间
     */
    public Timestamp F04;

}
