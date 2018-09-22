/*
 * 文 件 名:  BadClaimDetails.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月15日
 */
package com.dimeng.p2p.repeater.claim.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F12;

/**
 * <不良债权转让详情>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月15日]
 */
public class BadClaimZrDetails implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4751329206257070277L;
    
    /**
     * 借款账户
     */
    public String loanName;
    
    /**
     * 借款标题
     * 
     */
    public String loanTitle;
    
    /**
     * 债权价值（元）
     */
    public BigDecimal claimAmount = BigDecimal.ZERO;
    
    /**
     * 转让价格（元）
     */
    public BigDecimal transferAmount = BigDecimal.ZERO;
    
    /**
     * 担保机构
     * 
     */
    public String miga;
    
    /**
     * 担保方式
     * 
     */
    public T6230_F12 F12;
    
    /**
     * 是否有担保
     * 
     */
    public T6230_F11 F11;
}
