/*
 * 文 件 名:  Dbxxmx.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年1月8日
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <担保信息明细>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年1月8日]
 */
public class Dbxxmx extends AbstractEntity
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -3804150240476392803L;
    
    /**
     * 借款编号
     */
    public String jkbh;
    
    /**
     * 借款标题
     */
    public String jkbt;
    
    /**
     * 借款金额（元）
     */
    public BigDecimal jkje = new BigDecimal(0);
    
    /**
     * 年化利率
     */
    public BigDecimal nhl = new BigDecimal(0);
    
    /**
     * 期限
     */
    public String qx;
    
    /** 
     * 借款时间
     */
    public Timestamp jksj;
    
    /** 
     * 状态 
     */
    public String zt;
}
