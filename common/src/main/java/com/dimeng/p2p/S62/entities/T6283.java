package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Blob;

/** 
 * 企业融资意向附件表
 */
public class T6283 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 企业融资意向ID,参考T6281.F01
     */
    public int F02;

    /** 
     * 附件内容 
     */
    public Blob F03;

}
