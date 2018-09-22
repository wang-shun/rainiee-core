package com.dimeng.p2p.S66.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S66.enums.T6601_F06;

import java.sql.Timestamp;

/**
 * 定时任务管理表
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月26日]
 */
public class T6603 extends AbstractEntity
{
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID
     */
    public int F01;
    /** 
     * 任务ID
     */
    public int F02;
    /** 
     * 持续时间
     */
    public String F03;

    /** 
     * 任务最近运行结束时间
     */
    public Timestamp F04;
    /**
     * 任务最近运行开始时间
     */
    public Timestamp F05;

    
}
