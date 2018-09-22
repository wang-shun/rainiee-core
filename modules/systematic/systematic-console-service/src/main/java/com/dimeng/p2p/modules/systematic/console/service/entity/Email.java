package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SendType;

/**
 * 邮件推广
 * 
 * @author guopeng
 * 
 */
public class Email implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 邮件推广 ID
	 */
	public int id;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 内容
	 */
	public String content;
	/**
	 * 创建者
	 */
	public int createId;
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
	/**
	 * 发送对象（所有、指定人）
	 */
	public SendType sendType;
	/**
	 * 接受人数
	 */
	public int count;
	/**
	 * 邮箱账号
	 */
	public String[] emails;
	/**
	 * 发送人
	 */
	public String name;
}
