package com.dimeng.p2p.S50.entities
;

import com.dimeng.framework.service.AbstractEntity;
import java.sql.Timestamp;

/** 
 * 网签空白合同
 */
public class T5021 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 网签空白合同自增ID
     */
    public int F01;

    /** 
     * 合同版本
     */
    public String F02;

    /** 
     * 合同内容
     */
    public String F03;

    /** 
     * 创建者ID,参考T7011.F01
     */
    public int F04;

    /** 
     * 创建时间
     */
    public Timestamp F06;

    /** 
     * 最后更新时间
     */
    public Timestamp F07;

}
