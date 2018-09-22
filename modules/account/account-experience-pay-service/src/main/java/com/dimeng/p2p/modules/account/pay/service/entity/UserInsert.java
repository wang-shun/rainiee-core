package com.dimeng.p2p.modules.account.pay.service.entity;

import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6141_F03;

public abstract interface UserInsert {
	/**
	 * 账号名
	 * 
	 * @return
	 */
	public abstract String getAccountName();

	/**
	 * 密码
	 * 
	 * @return
	 */
	public abstract String getPassword();

	/**
	 * 邀请码
	 * 
	 * @return
	 */
	public abstract String getCode();
	
	
	/**
	 * 邀请人手机号
	 * 
	 * @return
	 */
	public abstract String getPhone();
	
	/**
	 * 邀请人姓名
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * 兴趣类型（LC:理财;JK:借款）
	 */
	public abstract T6141_F03 getType();
	
	    /**
     * 注册来源
     */
    public abstract T6110_F08 getRegisterType();
    
    /**
     * 业务员工号 
     * @return
     */
	public abstract String getNum();
}
