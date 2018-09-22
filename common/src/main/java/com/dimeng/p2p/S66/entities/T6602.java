package com.dimeng.p2p.S66.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
/**
 * 定时任务日志表
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月26日]
 */
public class T6602 extends AbstractEntity
{
    private static final long serialVersionUID = 1L;
    /** 
     * 自增ID
     */
    public int F01;
    
    /**
     * 定时任务ID（参考T6601.F01）
     */
    public int F02;
    /**
     * 异常内容
     */
    public String F03;
    /**
     * 创建时间
     */
    public Timestamp F04;
}
