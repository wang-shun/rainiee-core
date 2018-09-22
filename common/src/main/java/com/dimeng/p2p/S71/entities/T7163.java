package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 短信推广指定人列表
 */
public class T7163 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 短信推广指定人列表自增ID
     */
    public int F01;

    /** 
     * 短信推广ID,参考T7162.F01
     */
    public int F02;

    /** 
     * 指定人手机号
     */
    public String F03;

}
