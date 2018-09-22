/*
 * 文 件 名:  T7052.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月8日
 */
package com.dimeng.p2p.S70.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <逾期数据统计>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月8日]
 */
public class T7053 extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -5723251912645799173L;
    
    /**
     * 年份
     */
    public int F01;
    
    /**
     * 月份
     */
    public int F02;
    
    /**
     * 人数
     */
    public int F03;
    
    /**
     * 金额
     */
    public BigDecimal F04;
    
    /**
     * 最后更新时间
     */
    public Timestamp F05;
}
