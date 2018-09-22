package com.dimeng.p2p.account.user.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6171_F03;

public class ThirdUser extends User
{
    private static final long serialVersionUID = -8661769881937926648L;
    
    /**
     * 用户密码
     */
    public String F10;
    
    /**
     * 第三方用户唯一码
     */
    public String openId;
    
    /**
     * 是否授权
     */
    public T6171_F03 t6171_F03;
    
    /**
     * 最后登录时间
     */
    public Timestamp loginDate;
    
    /**
     * 是否qq授权
     */
    public String qqTen = "N";
    
    /**
     * 第三方登录授权码
     */
    public String token;
    
    /**
     * 授权码授权时间
     */
    public Long tokenTime;
    
    /**
     * 是否微信授权
     */
    public String wxAuth;
    
    /**
     * 账户状态
     */
    public T6110_F07 t6110_F07;
}