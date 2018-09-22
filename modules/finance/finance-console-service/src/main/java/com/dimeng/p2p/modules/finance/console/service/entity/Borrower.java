package com.dimeng.p2p.modules.finance.console.service.entity;

import java.io.Serializable;

/**
 * 借款人
 */
public class Borrower implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 真实姓名
	 */
	public String realName;
	/**
	 * 身份证id
	 */
	public String identifyId;
	/**
	 * 账号名
	 */
	public String accountName;
}
