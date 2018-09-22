package com.dimeng.p2p.S63.entities;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/**
 * 积分规则说明表
 */
public class T6354 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 积分说明
     */
    public String F02;

    /**
     * 积分规则
     */
    public String F03;

    /**
     * 创建时间
     */
    public Timestamp F04;

    /**
     * 最后修改时间
     */
    public Timestamp F05;

}
