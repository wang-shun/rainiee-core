package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SendType;

/**
 * 站内信推广
 * 
 * @author guopeng
 * 
 */
public class Letter implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 站内信推广 ID
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
	 * 用户名
	 */
	public String[] userNames;
	/**
	 * 发送数量
	 */
	public int count;
	/**
	 * 发送人
	 */
	public String name;
}
