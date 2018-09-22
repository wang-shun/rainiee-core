package com.dimeng.p2p.S62.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6290_F04;
import com.dimeng.p2p.S62.enums.T6290_F06;

/** 
 * 还款提醒设置
 */
public class T6290 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 自增ID
     */
    public int F01;
    
    /**
     * 提醒方式
     */
    public String F02;
    
    /**
     * 提醒天数
     */
    public int F03;
    
    /**
     * HKTX:还款提醒、YQTX:逾期提醒
     */
    public T6290_F04 F04;
    
    /**
     * 更新时间
     */
    public Timestamp F05;
    
    /**
     * 状态
     */
    public T6290_F06 F06;
}
