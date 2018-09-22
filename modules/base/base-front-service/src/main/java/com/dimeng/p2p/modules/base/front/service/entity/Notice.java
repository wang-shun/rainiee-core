package com.dimeng.p2p.modules.base.front.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

public class Notice implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 公告id
	 */
	public int id;
	/**
	 * 公告标题
	 */
	public String title;
	/**
	 * 时间
	 */
	public Timestamp time;
	
}
