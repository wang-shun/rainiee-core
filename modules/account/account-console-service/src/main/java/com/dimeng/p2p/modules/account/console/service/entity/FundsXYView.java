package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;

public class FundsXYView extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户ID,参考T6110.F01
	 */
	public int F01;

	/**
	 * 信用积分
	 */
	public int F02;

	/**
	 * 授信额度
	 */
	public BigDecimal F03 = new BigDecimal(0);

	/**
	 * 最后更新时间
	 */
	public Timestamp F04;

	/**
	 * 用户登录账号
	 */
	public String F05;

	/**
	 * 用户登录密码
	 */
	public String F06;

	/**
	 * 手机号码
	 */
	public String F07;

	/**
	 * 邮箱
	 */
	public String F08;

	/**
	 * 用户类型,ZRR:自然人;FZRR:非自然人
	 */
	public T6110_F06 F09;

	/**
	 * 状态,QY启用;SD:锁定;HMD:黑名单;
	 */
	public T6110_F07 F10;

	/**
	 * 注册来源,ZC:注册;HTTJ:后台添加
	 */
	public T6110_F08 F11;

	/**
	 * 注册时间
	 */
	public Timestamp F12;

	/**
	 * 担保方,S:是;F:否;
	 */
	public T6110_F10 F13;
    
    /**
     * 用户信用等级  
     */
    public String F14;

	/**
	 * 用户类型
	 */
	public String userType;
	/**
	 * 用户名或者企业名称
	 */
	public String userName;

}
