/*
 * 文 件 名:  RiskSetCondition.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月9日
 */
package com.dimeng.p2p.repeater.policy.entity;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <风险评估设置>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年3月9日]
 */
public class RiskSetCondition extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5197678953535078634L;
    
    /**
     * 保守型-最大值
     */
    public String maxBsx;
    
    /**
     * 谨慎型-最小值
     */
    public String minJsx;
    
    /**
     * 谨慎型-最大值
     */
    public String maxJsx;
    
    /**
     * 稳健型-最小值
     */
    public String minWjx;
    
    /**
     * 稳健型-最大值
     */
    public String maxWjx;
    
    /**
     * 进取型-最小值
     */
    public String minJqx;
    
    /**
     * 进取型-最大值
     */
    public String maxJqx;
    
    /**
     * 激进型-最小值
     */
    public String minJjx;
    
}
