package com.dimeng.p2p.modules.financial.front.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S64.entities.T6410;

public class YxxqEntity extends T6410 {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3230327519487560112L;
	/**
	 * 系统时间
	 */
	public Timestamp currentTime; 
	/**
	 * 进度
	 */
	public double proess;
	
	/**
	 * 标类型名称
	 */
	public String bidTypeName;
}
