package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6101_F03;
import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 资金账户
 */
public class T6101 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 账户ID,自增
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 账户类型,WLZH:往来账户;FXBZJZH:风险保证金账户;SDZH:锁定账户;
     */
    public T6101_F03 F03;

    /** 
     * 资金账号
     */
    public String F04;

    /** 
     * 账户名称
     */
    public String F05;

    /** 
     * 余额
     */
    public BigDecimal F06 = BigDecimal.ZERO;

    /** 
     * 最后更新时间
     */
    public Timestamp F07;

}
