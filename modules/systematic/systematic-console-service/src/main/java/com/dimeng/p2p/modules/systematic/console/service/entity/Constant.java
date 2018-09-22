/**
 * 
 */
package com.dimeng.p2p.modules.systematic.console.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @author guopeng
 * 
 */
public class Constant implements Serializable {

	private static final long serialVersionUID = 1L;
	/**
	 * ID,自增
	 */
	public int id;
	/**
	 * 常量KEY
	 */
	public String key;
	/**
	 * 常量描述
	 */
	public String desc;
	/**
	 * 修改前值
	 */
	public String value1;
	/**
	 * 修改后值
	 */
	public String value2;
	/**
	 * 创建时间
	 */
	public Timestamp updateTime;
	/**
	 * 修改人
	 */
	public String name;

}
