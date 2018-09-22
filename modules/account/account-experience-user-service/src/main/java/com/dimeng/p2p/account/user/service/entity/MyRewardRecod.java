/*
 * 文 件 名:  MyHbRecod.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2015年10月10日
 */
package com.dimeng.p2p.account.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S63.entities.T6342;

/**
 * <我的奖励>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年10月10日]
 */
public class MyRewardRecod extends T6342
{

    private static final long serialVersionUID = -3485262911435070073L;
    
    /**
     * 活动类型
     */
    public String type;
    
    /**
     * 价值：红包单位为元，加息卷单位是%
     */
    public BigDecimal value = BigDecimal.ZERO;
    
    /** 
     * 使用规则
     */
    public int useRule;
    
    /**
     * 投资使用规则（满多少就能使用）
     */
    public BigDecimal investUseRule = BigDecimal.ZERO;
    
    /**
     * 投资额度/倍数/最低充值额度
     */
    public BigDecimal quota = BigDecimal.ZERO;
    
    /**
     * 待收加息奖励
     */
    public BigDecimal dsJxjlAmount = BigDecimal.ZERO;
    
    /**
     * 已收加息奖励
     */
    public BigDecimal ysJxjlAmount = BigDecimal.ZERO;
    
}
