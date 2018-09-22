package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 个人学历记录
 */
public class T6142 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 学历记录ID,自增
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 毕业院校
     */
    public String F03;

    /** 
     * 入学年份
     */
    public int F04;

    /** 
     * 专业
     */
    public String F05;

    /** 
     * 在校情况简介
     */
    public String F06;
    
    /** 
     * 毕业年份
     */
    public int F07;

}
