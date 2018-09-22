package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 标抵押列表
 */
public class T6234 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 抵押物类型ID,参考T6213.F01
     */
    public int F03;

    /** 
     * 抵押物名称
     */
    public String F04;

}
