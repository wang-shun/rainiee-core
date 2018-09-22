package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6214_F04;

/** 
 * 抵押物属性
 */
public class T6214 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 抵押物类型ID,参考T6213.F01
     */
    public int F02;

    /** 
     * 属性名称
     */
    public String F03;
    

    /** 
     * 状态,QY:启用;TY:停用;
     */
    public T6214_F04 F04;

}
