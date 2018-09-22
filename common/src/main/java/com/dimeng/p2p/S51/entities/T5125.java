package com.dimeng.p2p.S51.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 协议范本
 */
public class T5125 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 协议范本ID，非自增 
     */
    public int F01;

    /** 
     * 最新版本号
     */
    public int F02;

    /** 
     * 类型名称
     */
    public String F03;

    /** 
     * 更新时间
     */
    public Timestamp F04;

}
