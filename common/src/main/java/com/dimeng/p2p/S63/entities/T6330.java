package com.dimeng.p2p.S63.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 业务员信息表
 */
public class T6330 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 业务员ID
     */
    public int F01;

    /** 
     * 姓名
     */
    public String F02;

    /** 
     * 手机号码
     */
    public String F03;

    /** 
     * 新增时间
     */
    public Timestamp F04;

    /** 
     * 最后更新时间
     */
    public Timestamp F05;

}
