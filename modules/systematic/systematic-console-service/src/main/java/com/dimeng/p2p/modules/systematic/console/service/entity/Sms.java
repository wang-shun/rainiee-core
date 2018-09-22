package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SendType;

/**
 * 短信推广
 * 
 * @author guopeng
 * 
 */
public class Sms implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 短信推广 ID
	 */
	public int id;
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
	 * 电话号码
	 */
	public String[] mobiles;
	/**
	 * 发送数量
	 */
	public int count;
	/**
	 * 发送人
	 */
	public String name;
}
