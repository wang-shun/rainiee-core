package com.dimeng.p2p.S65.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F07;
import com.dimeng.p2p.S65.enums.T6501_F11;

/** 
 * 订单表
 */
public class T6501 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 订单ID,自增
     */
    public int F01;
    
    /** 
     * 类型编码
     */
    public int F02;
    
    /** 
     * 状态,DTJ:待提交;YTJ:已提交;DQR:待确认;CG:成功;SB:失败;
     */
    public T6501_F03 F03;
    
    /** 
     * 创建时间
     */
    public Timestamp F04;
    
    /** 
     * 提交时间
     */
    public Timestamp F05;
    
    /** 
     * 完成时间
     */
    public Timestamp F06;
    
    /** 
     * 订单来源,XT:系统;YH:用户;HT:后台;
     */
    public T6501_F07 F07;
    
    /** 
     * 用户ID,参考T6110.F01
     */
    public int F08;
    
    /** 
     * 后台帐号ID,参考T7110.F01
     */
    public Integer F09;
    
    /** 
     * 流水号
     */
    public String F10;
    
    /**
     * 是否已对账, F：未对账; S:已对账;
     */
    public T6501_F11 F11;
    
    /**
     * 备注;
     */
    public String F12;
    
    /** 
     * 金额,此值只用来前台显示数据作用,禁止在后头代码拿此值赋值或计算,找对应的订单金额
     */
    public BigDecimal F13 = BigDecimal.ZERO;
    
    /**
     * 状态中文名称
     */
    public String status;
    
    /**
     * 订单类型中文名称
     */
    public String orderTypeName;
    
    /**
     * 托管请求第三方时订单号
     */
    public String F14;
}
