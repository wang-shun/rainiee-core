package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5120_F03;

/** 
 * 银行列表
 */
public class T5120 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 银行自增ID
     */
    public int F01;

    /** 
     * 银行名称
     */
    public String F02;

    /** 
     * 状态,QY:启用;TY:停用
     */
    public T5120_F03 F03;

}
