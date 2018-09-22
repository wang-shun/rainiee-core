package com.dimeng.p2p.S66.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S66.enums.T6601_F06;

/**
 * 定时任务管理表
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月26日]
 */
public class T6601 extends AbstractEntity
{
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID
     */
    public int F01;
    /** 
     * 任务名称
     */
    public String F02;
    /** 
     * 执行类
     */
    public String F03;
    /** 
     * 执行方法
     */
    public String F04;
    /** 
     * 执行时间
     */
    public String F05;
    /** 
     * 启用状态；ENABLE：启用，DISABLE：禁用
     */
    public T6601_F06 F06;
    /** 
     * 任务最近运行结束时间
     */
    public Timestamp F07;
    /**
     * 任务最近运行开始时间
     */
    public Timestamp F08;
    /** 
     * 创建时间
     */
    public Timestamp F09;
    /** 
     * 扩展参数
     */
    public String F10;
    
    /**
     * 备注
     */
    public String F11;
    
}
