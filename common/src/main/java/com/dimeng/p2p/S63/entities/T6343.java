package com.dimeng.p2p.S63.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
/**
 * 
 * 活动操作日志
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月9日]
 */
public class T6343 extends AbstractEntity
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 主键ID
     */
    public int F01;
    /**
     * 后台帐号ID,参考T7110.F01
     */
    public int F02;
    /**
     * 活动ID,参考T6340.F01
     */
    public int F03;
    /**
     * 操作时间
     */
    public Timestamp F04;
    /**
     * 操作描述
     */
    public String F05;
    
    /**
     * 下架、作废原因(活动结束时间;达到活动限制数量;同类型活动复盖;下架操作;作废操作;活动结束时间或达到活动限制数量)
     */
    public String F06;
    
}
