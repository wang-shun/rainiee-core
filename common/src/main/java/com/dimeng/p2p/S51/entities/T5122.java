package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5122_F03;

/** 
 * 交易类型
 */
public class T5122 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 交易类型编码,非自增
     */
    public int F01;

    /** 
     * 类型名称
     */
    public String F02;

    /** 
     * 状态,QY:启用;TY:停用;
     */
    public T5122_F03 F03;

}
