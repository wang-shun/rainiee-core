package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6241_F05;
import java.sql.Timestamp;

/** 
 * 标的审核记录
 */
public class T6241 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 标的ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 审核人,参考T7110.F01
     */
    public int F03;

    /** 
     * 反馈时间
     */
    public Timestamp F04;

    /** 
     * 状态,YCL:已处理;WCL:未处理;
     */
    public T6241_F05 F05;

    /** 
     * 审核意见
     */
    public String F06;

}
