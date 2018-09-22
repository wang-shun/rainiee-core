package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 用户推广信息
 */
public class T6111 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 推广码
     */
    public String F02;

    /** 
     * 邀请码
     */
    public String F03;

    /** 
     * 邀请人手机号
     */
    public String F04;

    /** 
     * 邀请人姓名
     */
    public String F05;

}
