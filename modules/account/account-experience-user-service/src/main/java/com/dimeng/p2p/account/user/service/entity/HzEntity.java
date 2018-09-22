package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.dimeng.p2p.S62.enums.T6285_F09;

public class HzEntity implements Serializable
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 债权ID
     */
    public String zrId;
    
    /**
     * 标题
     */
    public String title;
    
    /**
     * 类型
     */
    public String orderType;
    
    /**
     * 金额
     */
    public BigDecimal amount;
    
    /**
     * 还款日期
     */
    public Date skDate;
    
    /**
     * 状态
     */
    public T6285_F09 status;
    
}
