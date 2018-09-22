package com.dimeng.p2p.modules.account.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S61.enums.T6120_F05;

/**
 * 
 * 认证信息
 *
 */
public class Rzxx extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 类型名称
     */
    public String lxmc;
    
    /**
     * 必要认证
     */
    public T5123_F03 mustRz;
    
    /**
     * 分数
     */
    public int fs;
    
    /**
     * 认证次数
     */
    public int rzcs;
    
    /**
     * 认证状态
     */
    public T6120_F05 status;
    
    /**
     * 认证时间
     */
    public Timestamp time;
    
    /**
     * 有效记录ID
     */
    public int yxjlID;
    
    /**
     * 得分
     */
    public int ds;
    
    /**
     * 认证id
     */
    public int rzID;
    
}