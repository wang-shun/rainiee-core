/*
 * 文 件 名:  SetCondition.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2015年12月16日
 */
package com.dimeng.p2p.repeater.score.entity;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2015年12月16日]
 */
public class SetCondition extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 4169187648296286677L;
    
    //最小积分
    public String[] minScore;
    
    //最大积分
    public String[] maxScore;
    
    //最小金额
    public String[] minAmount;
    
    //最大金额
    public String[] maxAmount;

}
