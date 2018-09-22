package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 标的协议
 */
public class T6239 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 标的ID,参考T6230.F01
     */
    public int F01;

    /** 
     * 协议类型ID,参考T5125.F01
     */
    public int F02;

    /** 
     * 协议版本号
     */
    public int F03;

}
