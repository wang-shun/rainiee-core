package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6143_F03;

/** 
 * 个人工作记录
 */
public class T6143 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 工作记录ID,自增
     */
    public int F01;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;

    /** 
     * 工作状态,ZZ:在职;LZ:离职;
     */
    public T6143_F03 F03;

    /** 
     * 单位名称
     */
    public String F04;

    /** 
     * 职位
     */
    public String F05;

    /** 
     * 工作邮箱
     */
    public String F06;

    /** 
     * 工作城市,参考T5119.F01
     */
    public int F07;

    /** 
     * 工作地址
     */
    public String F08;

    /** 
     * 公司类别
     */
    public String F09;

    /** 
     * 公司行业
     */
    public String F10;

    /** 
     * 公司规模
     */
    public String F11;

}
