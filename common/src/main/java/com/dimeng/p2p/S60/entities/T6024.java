package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6024_F07;

/**
 * 用户银行卡
 */
public class T6024 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户银行卡自增ID
	 */
	public int F01;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F02;

	/**
	 * 开户银行ID,参考T6052.F01
	 */
	public int F03;

	/**
	 * 开户行地址(选择省市区)
	 */
	public String F04;

	/**
	 * 开户支行
	 */
	public String F05;

	/**
	 * 银行账号（唯一）
	 */
	public String F06;

	/**
	 * 状态,QY:启用;TY:停用;SC:删除;
	 */
	public T6024_F07 F07;

	/**
	 * 创建时间
	 */
	public Timestamp F08;

}
