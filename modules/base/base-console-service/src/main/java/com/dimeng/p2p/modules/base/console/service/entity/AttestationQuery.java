package com.dimeng.p2p.modules.base.console.service.entity;

import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S51.enums.T5123_F04;
import com.dimeng.p2p.S51.enums.T5123_F06;

/**
 * 认证信息
 *
 */
public interface AttestationQuery
{
    /**
     * 主键ID
     * @return
     */
    public int getId();
    
    /**
     * 认证项名称
     * @return
     */
    public String getName();
    
    /**
     * 必要认证,S:是;F:否
     * @return
     */
    public T5123_F03 getType();
    
    /**
     * 状态,QY:启用;TY:停用
     * @return
     */
    public T5123_F04 getStatus();
    
    /**
     * 最高分数
     * @return
     */
    public int getScore();
    
    /** 
     * 用户类型
     * @return
     */
    public T5123_F06 getUserType();
}
