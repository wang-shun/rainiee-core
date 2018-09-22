/*
 * 文 件 名:  T6246.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2015年3月9日
 */
package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6246_F07;

/**
 * 公益投资记录
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2015年3月9日]
 */
public class T6246 extends AbstractEntity
{

    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 自增ID
     */
    public int F01 ;
    /**
     * 公益标ID,参考T6242.F01
     */
    public int F02 ;
    /**
     * 投资人ID,参考T6110.F01
     */
    public int F03;
    /**
     * 捐助金额
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    
    /**
     * 债权金额
     */
    public BigDecimal F05 =BigDecimal.ZERO;
    
    /**
     * 捐助时间
     */
    public Timestamp F06;
    
    /**
     * 是否取消,F:否;S:是;
     */
    public T6246_F07 F07;
    
}
