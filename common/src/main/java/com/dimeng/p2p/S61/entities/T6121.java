package com.dimeng.p2p.S61.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6121_F05;

/** 
 * 用户认证记录
 */
public class T6121 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /** 
     * 用户ID,参考T6110.F01
     */
    public int F02;
    
    /** 
     * 认证项ID,参考T5123.F01
     */
    public int F03;
    
    /** 
     * 认证内容
     */
    public String F04;
    
    /** 
     * 认证结果,TG:通过;BTG:不通过;
     */
    public T6121_F05 F05;
    
    /** 
     * 认证人ID,参考T7110.F01
     */
    public int F06;
    
    /** 
     * 认证时间
     */
    public Timestamp F07;
    
    /** 
     * 认证标题
     */
    public String F08;
    
    /**
     * 附件编码
     */
    public String fileCode;
    
}
