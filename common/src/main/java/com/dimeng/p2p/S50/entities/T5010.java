package com.dimeng.p2p.S50.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S50.enums.T5010_F04;

/** 
 * 文章类别
 */
public class T5010 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 类别编码
     */
    public String F02;

    /** 
     * 类别名称
     */
    public String F03;

    /** 
     * 状态
     */
    public T5010_F04 F04;

    /** 
     * 父类id
     */
    public int F05;

    /**
     * URl
     */
    public String F06;

    /**
     * 权限ID
     */
    public String F07;

}
