/*
 * 文 件 名:  T6344.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年9月30日
 */
package com.dimeng.p2p.S63.entities;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6344_F09;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年9月30日]
 */
public class T6344 extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2183646556366703093L;
    
    /**
     * 主键ID
     */
    public int F01;
    
    /**
     * 活动id：参考T6340.F01
     */
    public int F02;
    
    /**
     * 发放数量
     */
    public int F03;
    
    /**
     * 使用有效期
     */
    public int F04;
    
    /**
     * 价值：红包单位为元，加息卷单位是%
     */
    public BigDecimal F05;
    
    /**
     * 投资、充值金额
     */
    public BigDecimal F06;
    
    /**
     * 投资使用规则（满多少就能使用）
     */
    public BigDecimal F07;

    /**
     * 已送数量
     */
    public int F08;
    
    /**
     * 使用有效期是否为按月计算,S:是;F:否
     */
    public T6344_F09 F09;

    /**
     * 体验金投资有效收益期
     */
    public int F10;

    /**
     * 体验金投资有效收益期计算方式(true:按月;false:按天)
     */
    public String F11;
}
