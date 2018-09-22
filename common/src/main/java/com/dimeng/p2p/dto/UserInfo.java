package com.dimeng.p2p.dto;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;


/** 
 * 个人基础信息
 */
public class UserInfo extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    public T6141 t6141;
    
    public T6110 t6110;
}
