package com.dimeng.p2p.S61.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 用户认证附件
 */
public class T6122 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 认证记录ID,参考T6021.F01
     */
    public int F01;
    
    /** 
     * 附件文件编码
     */
    public String F02;
    
    /** 
     * 附件大小
     */
    public int F03;
    
    /** 
     * 上传时间
     */
    public Timestamp F04;
    
    /** 
     * 附件格式
     */
    public String F05;
    
    /**
     * 自增id
     */
    public int F06;
    
}
