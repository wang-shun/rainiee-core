package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 邮件推广指定人列表
 */
public class T7165 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 邮件推广指定人列表自增ID
     */
    public int F01;

    /** 
     * 邮件推广ID,参考T7019.F01
     */
    public int F02;

    /** 
     * 指定人邮箱
     */
    public String F03;

}
