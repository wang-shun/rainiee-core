package com.dimeng.p2p.S61.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6106_F05;
import com.dimeng.p2p.common.enums.YesOrNo;

/**
 * 用户积分获取记录
 */
public class T6106 extends AbstractEntity
{
    
    private static final long serialVersionUID = 1L;
    
    public int F01;
    
    /**
     * 用户ID,参考T6110.F01
     */
    public int F02;
    
    /**
     * 积分
     */
    public int F03;
    
    /**
     * 获取时间
     */
    public Timestamp F04;
    
    /**
     * 积分类型:register:注册、sign:签到、invite:邀请、invest:投资、cellphone:手机认证、mailbox:邮箱认证、realname:实名认证
     * trusteeship:开通第三方托管账户、charge:充值、buygoods:现金购买商
     */
    public T6106_F05 F05;
    
    /**
     * 是否已清零，yes：是；no：否
     */
    public YesOrNo F06;
    
    /**
     * 最后修改时间
     */
    public Timestamp F07;
    
    /**
     * 登录名
     */
    public String F10;
    
    /**
     * 真实姓名
     */
    public String F11;
    
    /**
     * 操作人ID,参考T7110.F01
     */
    public int F12;
    
    /**
     * 操作人账号
     */
    public String F13;
    
}
