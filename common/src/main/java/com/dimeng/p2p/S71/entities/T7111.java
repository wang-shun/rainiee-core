package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S71.enums.T7110_F05;

import java.sql.Timestamp;

/**
 * 
 * 后台业务员锁定日志
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月2日]
 */
public class T7111 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 主键ID
     */
    public int F01;

    /** 
     * 业务员工号,参考T7110.F12
     */
    public String F02;

    /** 
     * 锁定时间
     */
    public Timestamp F03;

    /** 
     * 解锁时间
     */
    public Timestamp F04;

    /** 
     * 状态
     */
    public T7110_F05 F05;

}
