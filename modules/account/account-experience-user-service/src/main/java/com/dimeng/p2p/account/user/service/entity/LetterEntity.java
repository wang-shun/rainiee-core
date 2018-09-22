package com.dimeng.p2p.account.user.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.LetterStatus;

public class LetterEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * 站内信id
	 */
	public int id;
	/**
	 * 发送时间
	 */
	public Timestamp sendTime;
	/**
	 * 标题
	 */
	public String title;
	/**
	 * 状态
	 */
	public LetterStatus status;
	/**
	 * 内容
	 */
	public String content;
}
