package com.dimeng.p2p.S63.entities
        ;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6360_F03;
import java.sql.Timestamp;

/**
 * 商品订单操作日志
 */
public class T6360 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
     * 自增ID
     */
    public int F01;

    /**
     * 订单明细ID,参考T6359.F01
     */
    public int F02;

    /**
     * 状态（SH:审核,XG：修改,FH：发货,TH：退货,SQTK：申请退款,TK：退款，JJTK：拒绝退款）
     */
    public T6360_F03 F03;

    /**
     * 操作备注
     */
    public String F04;

    /**
     * 操作人，参考T7110.F01
     */
    public int F05;

    /**
     * 操作时间
     */
    public Timestamp F06;

}
