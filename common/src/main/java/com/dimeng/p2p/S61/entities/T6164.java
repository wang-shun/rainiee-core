package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 企业联系信息
 */
public class T6164 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 所在区域ID,参考T5119.F01
     */
    public int F02;

    /** 
     * 联系地址
     */
    public String F03;

    /** 
     * 联系电话
     */
    public String F04;

    /** 
     * 网站地址
     */
    public String F05;

    /** 
     * 法人联系电话
     */
    public String F06;
    
    /** 
     * 联系人
     */
    public String F07;
    
    /**
     * 所在区域地址
     */
    public String address;
    
    /**
     * 法人邮箱地址
     */
    public String email;

}
