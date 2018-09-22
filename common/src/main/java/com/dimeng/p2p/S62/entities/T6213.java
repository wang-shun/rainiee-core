package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6213_F03;

/** 
 * 抵押物类型
 */
public class T6213 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 类型名称
     */
    public String F02;

    /** 
     * 状态,QY:启用;TY:停用;
     */
    public T6213_F03 F03;

}
