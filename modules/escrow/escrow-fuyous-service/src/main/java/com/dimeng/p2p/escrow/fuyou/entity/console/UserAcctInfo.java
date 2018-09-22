package com.dimeng.p2p.escrow.fuyou.entity.console;

import java.io.Serializable;

public class UserAcctInfo implements Serializable
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    public String platformId;
    
    /**
     * 用户认证状态
     */
    public String userIdentiState;
    
    /**
     * 手机号码
     */
    public String phoneNum;
    
    /**
     * 手机认证状态
     */
    public String phoneIdentiState;
    
    /**
     * 邮箱地址
     */
    public String email;
    
    /**
     * 邮箱认证状态
     */
    public String emailIdentiState;
}
