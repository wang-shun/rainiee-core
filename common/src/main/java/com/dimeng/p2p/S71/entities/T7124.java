package com.dimeng.p2p.S71.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S71.enums.T7124_F04;
import java.sql.Timestamp;

/** 
 * 实名认证日志
 */
public class T7124 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 自增ID
     */
    public int F01;

    /** 
     * 身份证号
     */
    public String F02;

    /** 
     * 姓名
     */
    public String F03;

    /** 
     * 认证结果,TG:认证通过;SB:认证失败;
     */
    public T7124_F04 F04;

    /** 
     * 错误代码
     */
    public int F05;

    /** 
     * 认证时间
     */
    public Timestamp F06;

}
