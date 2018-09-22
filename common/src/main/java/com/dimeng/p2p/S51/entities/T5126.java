package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 协议内容表
 */
public class T5126 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 范本类型ID,参考T5125.F01
     */
    public int F01;

    /** 
     * 版本号
     */
    public int F02;

    /** 
     * 模板内容
     */
    public String F03;

    /** 
     * 最后更新时间
     */
    public Timestamp F04;

}
