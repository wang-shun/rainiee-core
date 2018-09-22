package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S71.entities.T7151;

/**
 * 黑名单列表
 * 
 * @author gongliang
 * 
 */
public class BlacklistInfo extends T7151
{
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 拉黑人用户名
     */
    public String lhName;
    
    /**
     * 被拉黑用户名
     */
    public String accountName;
    
    /**
     * 手机号
     */
    public String phone;
    
    /**
     * 邮箱
     */
    public String email;
    
    /**
     * 用户类型
     */
    public T6110_F06 userType;
    
    /**
     * 担保方
     */
    public T6110_F10 dbf;
    
    /**
     * 身份证号码
     */
    public String idCard;
    
    /**
     * 待还总额（元）
     */
    public BigDecimal whMoney = BigDecimal.ZERO;
    
    /**
     * 真实姓名
     */
    public String realName;
    
}
