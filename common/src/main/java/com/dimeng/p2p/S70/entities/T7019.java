package com.dimeng.p2p.S70.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7019_F07;

/**
 * 邮件推广
 */
public class T7019 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 邮件推广自增ID
	 */
	public int F01;

	/**
	 * 标题
	 */
	public String F02;

	/**
	 * 内容
	 */
	public String F03;

	/**
	 * 接受人数
	 */
	public int F04;

	/**
	 * 创建者,参考T7011.F01
	 */
	public int F05;

	/**
	 * 创建时间
	 */
	public Timestamp F06;

	/**
	 * 发送对象,SY:所有;ZDR:指定人
	 */
	public T7019_F07 F07;

}
