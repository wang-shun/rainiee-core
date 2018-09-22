package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6120_F05;
import java.sql.Timestamp;

/** 
 * 用户认证信息
 */
public class T6120 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,参考T6110.F01
     */
    public int F01;

    /** 
     * 认证项ID,参考T5123.F01
     */
    public int F02;

    /** 
     * 认证次数
     */
    public int F03;

    /** 
     * 积分
     */
    public int F04;

    /** 
     * 认证状态,WYZ:未验证;DSH:待审核;TG:通过;BTG:不通过
     */
    public T6120_F05 F05;

    /** 
     * 认证时间
     */
    public Timestamp F06;

    /** 
     * 有效记录ID,,参考T6121.F01
     */
    public int F07;

}
