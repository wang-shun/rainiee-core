package com.dimeng.p2p.S63.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6353_F05;

import java.sql.Timestamp;

/**
 * 积分范围表
 */
public class T6353 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 积分最小值
     */
    public String F02;

    /**
     * 积分最大值
     */
    public String F03;

    /**
     * 创建时间
     */
    public Timestamp F04;

    /**
     * score:积分、amount:金额
     */
    public T6353_F05 F05;
}
