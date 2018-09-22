package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 回访记录表
 */
public class T7168 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 用户ID（T6110表）
     */
    public int F02;

    /** 
     * 回访详情
     */
    public String F03;

    /** 
     * 回访时间
     */
    public Timestamp F04;
    
    /** 
     * 记录时间
     */
    public Timestamp F05;

}
