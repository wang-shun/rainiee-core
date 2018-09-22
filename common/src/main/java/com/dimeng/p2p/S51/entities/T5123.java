package com.dimeng.p2p.S51.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S51.enums.T5123_F03;
import com.dimeng.p2p.S51.enums.T5123_F04;
import com.dimeng.p2p.S51.enums.T5123_F06;

/** 
 * 信用认证项
 */
public class T5123 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    /** 
     * 认证ID,非自增
     */
    public int F01;
    
    /** 
     * 类型名称
     */
    public String F02;
    
    /** 
     * 必要认证,S:是;F:否
     */
    public T5123_F03 F03;
    
    /** 
     * 状态,QY:启用;TY:停用
     */
    public T5123_F04 F04;
    
    /** 
     * 最高分数
     */
    public int F05;
    
    /**
     * 用户类型
     */
    public T5123_F06 F06;
    
}
