package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 后台操作日志
 */
public class T7120 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 主键ID
     */
    public int F01;

    /** 
     * 后台帐号ID,参考T7110.F01
     */
    public int F02;

    /** 
     * 操作时间
     */
    public Timestamp F03;

    /** 
     * 操作类型
     */
    public String F04;

    /** 
     * 操作描述
     */
    public String F05;

    /** 
     * 访问IP
     */
    public String F06;

}
