package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6250_F07;
import com.dimeng.p2p.S62.enums.T6250_F08;
import com.dimeng.p2p.S62.enums.T6250_F09;
import com.dimeng.p2p.S62.enums.T6250_F11;
import com.dimeng.p2p.common.enums.BusinessStatus;

/**
 * 投资记录
 */
public class T6250 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 自增ID
     */
    public int F01;
    
    /**
     * 标ID,参考T6230.F01
     */
    public int F02;
    
    /**
     * 投资人ID,参考T6110.F01
     */
    public int F03;
    
    /**
     * 购买价格
     */
    public BigDecimal F04 = BigDecimal.ZERO;
    
    /**
     * 债权金额
     */
    public BigDecimal F05 = BigDecimal.ZERO;
    
    /**
     * 投资时间
     */
    public Timestamp F06;
    
    /**
     * 是否取消,F:否;S:是;
     */
    public T6250_F07 F07;
    
    /**
     * 是否已放款,F:否;S:是;
     */
    public T6250_F08 F08;
    
    /**
     * 是否自动投资,F:否;S:是;
     */
    public T6250_F09 F09;
    
    /**
     * 预授权合同号
     */
    public String F10;
    
    /**
     * 标状态,参考T6230.F20
     */
    public T6230_F20 F20;
    
    /**
     * 来源,PC:PC;APP:APP;WEIXIN:微信;
     */
    public T6250_F11 F11;
    
    /**
     * 业务员工号
     */
    public String F12;
    
    /**
     * 业务员状态
     */
    public BusinessStatus F13;
    
}
