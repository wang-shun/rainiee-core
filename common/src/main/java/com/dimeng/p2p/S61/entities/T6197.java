/*
 * 文 件 名:  T6197.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月10日
 */
package com.dimeng.p2p.S61.entities;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * <运营数据累计投资设置>
 * <功能详细描述>
 * 
 * @author  zhoucl
 * @version  [版本号, 2016年3月10日]
 */
public class T6197 extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1037037429249159241L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /** 
     * 累计投资金额
     */
    public BigDecimal F02 = BigDecimal.ZERO;
    
    /** 
     * 累计投资日期
     */
    public Date F03;
    
    /** 
     * 最后操作时间
     */
    public Timestamp F04;
    
}
