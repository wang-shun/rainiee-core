/*
 * 文 件 名:  T6264.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年6月15日
 */
package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6264_F04;

/**
 * <不良债权转让申请>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年6月15日]
 */
public class T6264 extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 7858330280652826785L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /** 
     * 债权编号
     */
    public String F02;
    
    /** 
     * 标的ID
     */
    public int F03;
    
    /** 
     * 状态
     */
    public T6264_F04 F04;
    
    /** 
     * 逾期天数
     */
    public int F05;
    
    /** 
     * T6252 id
     */
    public int F06;
    
    /** 
     * 申请时间
     */
    public Timestamp F07;
    
    /** 
     * 操作时间
     */
    public Timestamp F08;
    
    /** 
     * 债权价值
     */
    public BigDecimal F09 = BigDecimal.ZERO;
    
    /** 
     * 转让价格
     */
    public BigDecimal F10 = BigDecimal.ZERO;
    
    /** 
     * 备注（审核不通过原因，手动下架，自动下架）
     */
    public String F11;
    
    /** 
     * 第三方债权使用编码
     */
    public String F12;
}
