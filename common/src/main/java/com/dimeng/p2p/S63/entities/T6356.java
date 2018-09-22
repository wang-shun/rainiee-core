package com.dimeng.p2p.S63.entities
        ;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6356_F03;
import com.dimeng.p2p.S63.enums.T6356_F04;
import java.sql.Timestamp;

/**
 * 积分规则设置表
 */
public class T6356 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    public int F01;

    /**
     * 积分值
     */
    public String F02;

    /**
     * register:注册、sign:签到、invite:邀请、invest:投资、cellphone:手机认证、mailbox:邮箱认证、realname:实名认证）
     * trusteeship:开通第三方托管账户、charge:充值、buygoods:现金购买商
     */
    public T6356_F03 F03;

    /**
     * on:启用、off:停用）
     */
    public T6356_F04 F04;

    /**
     * 创建时间
     */
    public Timestamp F05;

    /**
     * 最后修改时间
     */
    public Timestamp F06;

}
