package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6215_F03;

/** 
 * 标费率项目
 */
public class T6215 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 费率项目
     */
    public String F02;

    /** 
     * 状态,QY:启用;TY:停用;
     */
    public T6215_F03 F03;

}
