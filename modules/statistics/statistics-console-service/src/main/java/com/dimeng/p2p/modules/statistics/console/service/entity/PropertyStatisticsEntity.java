package com.dimeng.p2p.modules.statistics.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

public class PropertyStatisticsEntity extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
    * 用户ID,自增
    */
    public int id;
    
    /**
     * 法人身份证号码
     */
    public String idCardNo;
    
    /**
     * 用户登录账号
     */
    public String userName;
    
    /**
     * 企业（机构）名称
     */
    public String businessName;
    
    /**
     * 法人手机号码
     */
    public String phone;
    
    /**
     * 法人邮箱
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
    public BigDecimal earningsAmount = BigDecimal.ZERO;
    
    /**
     * 总充值
     */
    public BigDecimal payAmount = BigDecimal.ZERO;
    
    /**
     * 总提现
     */
    public BigDecimal withdrawAmount = BigDecimal.ZERO;
    
    /**
     * 风险保证金余额
     */
    public BigDecimal riskAssureAmount = BigDecimal.ZERO;
    
    /**
     * 垫付总金额
     */
    public BigDecimal advanceAmount = BigDecimal.ZERO;
    
    /**
     * 可用余额最小值
     */
    public BigDecimal balanceMin = BigDecimal.ZERO;
    
    /**
     * 可用余额最大值
     */
    public BigDecimal balanceMax = BigDecimal.ZERO;
    
    /**
     * 垫付待还总金额
     */
    public BigDecimal advanceUnpaidAmount = BigDecimal.ZERO;
}
