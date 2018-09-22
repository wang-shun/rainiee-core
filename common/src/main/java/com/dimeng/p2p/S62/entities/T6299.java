package com.dimeng.p2p.S62.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6299_F04;

/** 
 * 筛选条件
 */
public class T6299 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 自增ID
     */
    public int F01;
    
    /**
     * 搜索下线
     */
    public int F02;
    
    /**
     * 搜索上线
     */
    public int F03;
    
    /** 
     * 筛选类型,JKQX:借款期限;RZJD:融资进度;NHSY:年化利率
     */
    public T6299_F04 F04;
    
    /** 
     * 最后修改时间
     */
    public Timestamp F05;
    
}
