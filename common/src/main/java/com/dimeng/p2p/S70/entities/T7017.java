package com.dimeng.p2p.S70.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7017_F06;

/**
 * 短信推广
 */
public class T7017 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信推广自增ID
	 */
	public int F01;

	/**
	 * 内容
	 */
	public String F02;

	/**
	 * 接受人数
	 */
	public int F03;

	/**
	 * 创建者,参考T7011.F01
	 */
	public int F04;

	/**
	 * 创建时间
	 */
	public Timestamp F05;

	/**
	 * 发送对象,SY:所有;ZDR:指定人
	 */
	public T7017_F06 F06;

}
