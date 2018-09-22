package com.dimeng.p2p.modules.financing.user.service.entity;

import java.io.Serializable;
/**
 * 优选理财加入用户信息
 */
public class YxUser implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 真实姓名
	 */
	public String name;
	/**
	 * 身份证
	 */
	public String identifyId;
	/**
	 * 电话
	 */
	public String phone;
}
