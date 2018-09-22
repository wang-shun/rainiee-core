package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6270_F05;
import java.sql.Timestamp;

/** 
 * 提前还款申请
 */
public class T6270 extends AbstractEntity{

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
     * 期数
     */
    public int F03;

    /** 
     * 申请时间
     */
    public Timestamp F04;

    /** 
     * 状态,DSH:待审核;YHK:已还款;BTG:不通过;
     */
    public T6270_F05 F05;

    /** 
     * 审核人ID,参考T7110.F01
     */
    public int F06;

    /** 
     * 审核时间
     */
    public Timestamp F07;

    /** 
     * 审核意见
     */
    public String F08;

    /** 
     * 备注
     */
    public String F09;

}
