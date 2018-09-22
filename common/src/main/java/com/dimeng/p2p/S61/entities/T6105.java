package com.dimeng.p2p.S61.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户积分账户
 */
public class T6105 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 用户ID，参考T6110.F01
     */
    public int F02;

    /**
     * 总积分
     */
    public int F03;

    /**
     * 已用积分
     */
    public int F04;

    /**
     * 兑换次数
     */
    public int F05;

    /**
     * 创建时间
     */
    public Timestamp F06;

    /**
     * 最后更新时间
     */
    public Timestamp F07;
    
    /**
     * 登录名
     */
    public String F08;
    
    /**
     * 真实姓名
     */
    public String F09;
    
    /**
     * 可用积分
     */
    public int F10;
    
}
