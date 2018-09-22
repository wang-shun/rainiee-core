package com.dimeng.p2p.S61.entities;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户风险评估记录明细表
 */
public class T6150 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 用户风险评估记录表ID,T6147.F01
     */
    public int F02;

    /**
     * 风险评估问题信息表ID,T6149.F01
     */
    public int F03;

    /**
     * 评估问题选项：A,B,C,D
     */
    public String F04;

}
