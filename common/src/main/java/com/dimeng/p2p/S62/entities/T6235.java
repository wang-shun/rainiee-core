package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 标抵押属性
 */
public class T6235 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 标抵押物ID,参考T6234.F01
     */
    public int F02;

    /** 
     * 抵押物属性ID,参考T6214.F01
     */
    public int F03;

    /** 
     * 属性值
     */
    public String F04;
    
    /**
     * 标ID
     */
    public int F05;

}
