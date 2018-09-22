package com.dimeng.p2p.account.user.service.entity;

import com.dimeng.p2p.S62.entities.T6253;
import com.dimeng.p2p.S62.enums.T6230_F20;

public class Dfxxmx extends T6253
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 用户名
     */
    public String userName;
    
    /**
     * 状态
     */
    public T6230_F20 status;
    
    /**
     * 借款标题
     */
    public String title;
    
    /**
     * JSON 垫付状态
     */
    public String state;
    
    /**
     * 垫付合同路径
     */
    public String contractURL;
}
