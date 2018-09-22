package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 用户信用档案表
 */
public class T6144 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户账号ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 逾期次数
     */
    public int F02;

    /** 
     * 严重逾期次数
     */
    public int F03;

    /** 
     * 最长逾期天数
     */
    public int F04;

    /** 
     * 最后更新时间
     */
    public Timestamp F05;

}
