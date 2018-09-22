package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6116_F05;

import java.math.BigDecimal;
import java.sql.Timestamp;

/** 
 * 用户信用账户
 */
public class T6116 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 信用积分
     */
    public int F02;

    /** 
     * 授信额度
     */
    public BigDecimal F03 = BigDecimal.ZERO;

    /** 
     * 最后更新时间
     */
    public Timestamp F04;

    /**
     * 信用等级
     */
    public T6116_F05 F05;

}
