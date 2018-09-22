package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.SysMessageStatus;
import com.dimeng.p2p.common.enums.SysMessageType;

/**
 * 信息模板
 * 
 * @author guopeng
 * 
 */
public class Template implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 系统消息模板表 key
	 */
	public String key;
	/**
	 * 消息类型（站内信、邮件、短信）
	 */
	public SysMessageType type;
	/**
	 * 事件描述
	 */
	public String eventDes;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 模板内容
	 */
	public String content;
	/**
	 * 模板参数描述
	 */
	public String paramDes;
	/**
	 * 最后更新时间
	 */
	public Timestamp lastUpdateTime;
	/**
	 * 默认模板内容
	 */
	public String defalutContent;
	/**
	 * 启用状态
	 */
	public SysMessageStatus status;
}
