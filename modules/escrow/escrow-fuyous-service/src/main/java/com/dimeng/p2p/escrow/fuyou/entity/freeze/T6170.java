package com.dimeng.p2p.escrow.fuyou.entity.freeze;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.escrow.fuyou.entity.enums.T6170_F06;

/**
 * 资金冻结管理
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月2日]
 */
public class T6170 extends AbstractEntity
{
    
    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * ID,自增
     */
    public int F01;
    
    /**
     * 请求流水号
     */
    public String F02;
    
    /**
     * 登陆账号
     */
    public String F03;
    
    /**
     * 冻结金额
     */
    public BigDecimal F04;
    
    /**
     * 冻结时间
     */
    public Timestamp F05;
    
    /**
     * 冻结状态
     */
    public T6170_F06 F06;
    
}
