package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 常量变更日志
 */
public class T7121 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * KEY值
     */
    public String F02;

    /** 
     * 常量名称
     */
    public String F03;

    /** 
     * 修改前值
     */
    public String F04;

    /** 
     * 修改后值
     */
    public String F05;

    /** 
     * 修改人ID,参考T7110.F01
     */
    public int F06;

    /** 
     * 修改时间
     */
    public Timestamp F07;

}
