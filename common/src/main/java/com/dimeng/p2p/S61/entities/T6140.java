package com.dimeng.p2p.S61.entities
;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6140_F08;
import com.dimeng.p2p.S61.enums.T6140_F10;

/** 
 * 用户快捷支付信息
 */
public class T6140 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户快捷支付ID,自增ID
     */
    public int F01;

    /** 
     * 用户ID_customerId客户号,客户号,参考T6110.F01
     */
    public int F02;

    /** 
     * 银行名称,参考T5120.F02
     */
    public String F03;

    /** 
     * 银行预留手机号
     */
    public String F04;


    /** 
     * 快捷支付银行卡短号
     */
    public String F05;

    /** 
     * 银行卡号,前4位,后3位保留,其他星号代替
     */
    public String F06;

    /** 
     * 银行卡号,加密存储
     */
    public String F07;

    /** 
     * 快捷支付银行卡 状态,QY:启用;TY:停用;
     */
    public T6140_F08 F08;

    /** 
     * 创建时间
     */
    public Timestamp F09;

    /** 
     * 快捷支付 认证, WRZ未认证;TG:通过;BTG:不通过;
     */
    public T6140_F10 F10;

    /**
     * 手机令牌信息
     */
    public String F11;
    
    /**
     * 快捷支付鉴权跟踪号
     */
    public String F12;
    
}
