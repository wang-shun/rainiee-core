package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

public class GrMoneyEntity extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
    * 用户ID,自增
    */
    public int id;
    
    /**
     * 真实姓名
     */
    public String name;
    
    /**
     * 身份证号码
     */
    public String sfzh;
    
    /**
     * 用户登录账号
     */
    public String userName;
    
    /**
     * 手机号码
     */
    public String phone;
    
    /**
     * 邮箱
     */
    public String email;
    
    /**
     * 注册开始时间
     */
    public Timestamp startTime;
    
    /**
     * 可用余额
     */
    public BigDecimal balance = BigDecimal.ZERO;
    
    /**
     * 冻结资金
     */
    public BigDecimal freezeFunds = BigDecimal.ZERO;
    
    /**
     * 理财资产
     */
    public BigDecimal tzzc = BigDecimal.ZERO;
    
    /**
     * 负债资产
     */
    public BigDecimal loanAmount = BigDecimal.ZERO;
    
    /**
     * 总收益
     */
    public BigDecimal totalIncome = BigDecimal.ZERO;
    
    /**
     * 总充值
     */
    public BigDecimal totalRecharge = BigDecimal.ZERO;
    
    /**
     * 总提现
     */
    public BigDecimal totalWithdraw = BigDecimal.ZERO;
    
    /**
     * 查询可用余额起始值 
     */
    public BigDecimal begin = BigDecimal.ZERO;
    
    /**
     * 查询可用余额结束值 
     */
    public BigDecimal end = BigDecimal.ZERO;
    
    /**
     * 风险保证金余额
     */
    public BigDecimal riskAssureAmount = BigDecimal.ZERO;
    
    /**
     * 垫付总金额
     */
    public BigDecimal advanceAmount = BigDecimal.ZERO;
    
    /**
     * 垫付待还总金额
     */
    public BigDecimal advanceUnpaidAmount = BigDecimal.ZERO;
}
