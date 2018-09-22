package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 用户第三方托管帐号
 */
public class T6119 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 托管机构代码
     */
    public int F02;

    /** 
     * 托管帐号ID
     */
    public String F03;
    
    /** 
     * 托管授权情况
     */
    public String F04;

}
