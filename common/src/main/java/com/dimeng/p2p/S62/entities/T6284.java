package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6284_F08;
import java.sql.Timestamp;

/** 
 * 企业投资意向
 */
public class T6284 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 联系人
     */
    public String F02;

    /** 
     * 联系电话
     */
    public String F03;

    /** 
     * 手机号码
     */
    public String F04;

    /** 
     * 邮箱地址
     */
    public String F05;

    /** 
     * 所在区域ID，参考T5019.F01
     */
    public int F06;

    /** 
     * 借款描述
     */
    public String F07;

    /** 
     * 处理状态,WCL:未处理;YCL:已处理
     */
    public T6284_F08 F08;

    /** 
     * 处理人,参考T7110.F01
     */
    public int F09;

    /** 
     * 申请时间
     */
    public Timestamp F10;

    /** 
     * 处理时间
     */
    public Timestamp F11;

    /** 
     * 处理结果
     */
    public String F12;

}
