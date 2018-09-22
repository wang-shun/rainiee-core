package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S71.enums.T7164_F07;
import java.sql.Timestamp;

/** 
 * 邮件推广
 */
public class T7164 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 邮件推广自增ID
     */
    public int F01;

    /** 
     * 标题
     */
    public String F02;

    /** 
     * 内容
     */
    public String F03;

    /** 
     * 接收人数
     */
    public int F04;

    /** 
     * 创建者,参考T7110.F01
     */
    public int F05;

    /** 
     * 创建时间
     */
    public Timestamp F06;

    /** 
     * 发送对象,SY:所有;ZDR:指定人
     */
    public T7164_F07 F07;

}
