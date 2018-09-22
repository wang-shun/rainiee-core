package com.dimeng.p2p.S61.entities
;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6145_F07;

import java.sql.Timestamp;

/** 
 * 用户认证视频表
 */
public class T6145 extends AbstractEntity{

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
     * 视频文件格式
     */
    public String F03;

    /** 
     * 视频文件编码
     */
    public String F04;

    /** 
     * 视频文件大小
     */
    public int F05;
    
    /**
     * 上传时间
     */
    public Timestamp F06;
    
    /**
     * 审核状态,DSH:待审核;SHTG:审核通过;SHBTG:审核不通过
     */
    public T6145_F07 F07;
    
    /**
     * 审核意见
     */
    public String F08;
}
