package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * 系统常量
 * 
 * @author guopeng
 * 
 */
public class SysConstant implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * ID,自增
	 */
	public int id;
	/**
	 * 常量名称
	 */
	public String name;
	/**
	 * 常量描述
	 */
	public String desc;
	/**
	 * 常量值
	 */
	public String value;
	/**
	 * 发布者
	 */
	public int operator;
	/**
	 * 创建时间
	 */
	public Timestamp createTime;
}
