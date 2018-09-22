package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

public class FundsXYJYView extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 交易类型名称
     */
    public String F02;

    /** 
     * 发生时间
     */
    public Timestamp F03;

    /** 
     * 收入
     */
    public BigDecimal F04 = new BigDecimal(0);

    /** 
     * 支出
     */
    public BigDecimal F05 = new BigDecimal(0);

    /** 
     * 余额
     */
    public BigDecimal F06 = new BigDecimal(0);

    /** 
     * 备注
     */
    public String F07;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F08;

    /** 
     * 信用积分
     */
    public int F09;

    /** 
     * 授信额度
     */
    public BigDecimal F10 = new BigDecimal(0);

    /** 
     * 最后更新时间
     */
    public Timestamp F11;

}
