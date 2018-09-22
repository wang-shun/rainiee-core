package com.dimeng.p2p.escrow.fuyou.entity.entities;

import java.io.Serializable;

/**
 * 认证
 * 
 * @author heshiping
 * 
 *         2015-04-28
 * 
 */
public class Auth implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 手机是否认证
	 */
	public boolean phone = false;

	/**
	 * 是否实名认证
	 */
	public boolean realName = false;
	/**
	 * 是否设置交易密码
	 */
	 public boolean withdrawPsw = false;

}
