package com.dimeng.p2p.modules.capital.user.service.entity;

import java.io.Serializable;

public class BankCard implements Serializable{

	private static final long serialVersionUID = 1L;
	/**
	 * 银行卡记录id
	 */
	public int id;
	/**
	 * 开户银行
	 */
	public String bank;
	/**
	 * 开户行id
	 */
	public int bankId;
	/**
	 * 银行卡号
	 */
	public String cardNumber;
}
