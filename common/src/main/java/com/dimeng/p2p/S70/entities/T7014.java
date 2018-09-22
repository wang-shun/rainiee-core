package com.dimeng.p2p.S70.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7014_F02;
import com.dimeng.p2p.S70.enums.T7014_F09;

/**
 * 推广消息模版
 */
public class T7014 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统消息模板表key
	 */
	public String F01;

	/**
	 * 消息类型,ZNX:站内信;YJ:邮件;DX:短信
	 */
	public T7014_F02 F02;

	/**
	 * 事件描述
	 */
	public String F03;

	/**
	 * 标题
	 */
	public String F04;

	/**
	 * 模板内容
	 */
	public String F05;

	/**
	 * 模板参数描述
	 */
	public String F06;

	/**
	 * 最后更新时间
	 */
	public Timestamp F07;

	/**
	 * 默认模板内容
	 */
	public String F08;

	/**
	 * 是否启用,QY:启用;TY:停用
	 */
	public T7014_F09 F09;

}
