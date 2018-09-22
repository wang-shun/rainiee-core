package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5124_F05;

/** 
 * 信用等级
 */
public class T5124 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 等级名称
     */
    public String F02;

    /** 
     * 下限分数
     */
    public int F03;

    /** 
     * 上限分数
     */
    public int F04;

    /** 
     * 状态,QY:启用;TY:停用;
     */
    public T5124_F05 F05;

}
