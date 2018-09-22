package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5117_1_F01;

/** 
 * 协议附件表
 */
public class T5117_1 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 协议类型,BXBZ:本息保障;ZCXY:注册协议
     */
    public T5117_1_F01 F01;

    /** 
     * 附件编码
     */
    public String F02;

}
