package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S71.enums.T7152_F04;
import java.sql.Timestamp;

/** 
 * 催收记录表
 */
public class T7152 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 催收记录表自增ID
     */
    public int F01;

    /** 
     * 借款标ID,参考T6230.F01
     */
    public int F02;

    /** 
     * 借款人ID,参考T6010.F01
     */
    public int F03;

    /** 
     * 催收方式,DH:电话;XC:现场;FL法律
     */
    public T7152_F04 F04;

    /** 
     * 催收人
     */
    public String F05;

    /** 
     * 结果概要
     */
    public String F06;

    /** 
     * 备注
     */
    public String F07;

    /** 
     * 催收时间
     */
    public Timestamp F08;

    /** 
     * 操作人,参考T7011.F01
     */
    public int F09;

    /** 
     * 操作时间
     */
    public Timestamp F10;

}
