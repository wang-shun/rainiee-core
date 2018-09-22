/*
 * 文 件 名:  Dfxxmx.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoucl
 * 修改时间:  2016年1月8日
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <垫付信息明细>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年1月8日]
 */
public class Dfxxmx extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = -7855973830515496865L;

    /**
     * 借款编号
     */
    public String jkbh;
          
    /**
     * 借款标题
     */
    public String jkbt;
    
    /**
     * 用户名
     */
    public String yhm;
    
    /**
     * 垫付金额（元）
     */
    public BigDecimal dfje = new BigDecimal(0);
    
    /**
     * 垫付返回金额（元）
     */
    public BigDecimal dffhje = new BigDecimal(0);
    
    /**
     * 逾期期数（期）
     */
    public int yqqs;
    
    /** 
     * 状态 
     */
    public String zt;
}
