package com.dimeng.p2p.S50.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S50.enums.T5020_F03;

/** 
 * 银行列表
 */
public class T5020 extends AbstractEntity{

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
    public T5020_F03 F03;

    /** 
     * 代码
     */
    public String F04;
    /**
     * 代付银行编码
     */
    public String F05;
    
    /**
     * 是否支持快捷支付
     */
    public int F06;

}
