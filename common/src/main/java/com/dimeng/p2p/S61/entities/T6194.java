package com.dimeng.p2p.S61.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
/**
 * 密码问题与答案
 * @author heluzhu
 *
 */
public class T6194 extends AbstractEntity {

	private static final long serialVersionUID = 1L;
	/**
	 * 主键，自增ID
	 */
	public int F01;
	/**
	 * 用户ID,T6110.F01
	 */
	public int F02;
	/**
	 * 问题ID,T6193.F01
	 */
	public int F03;
	/**
	 * 问题回答
	 */
	public String F04;
	/**
	 * 最新更新时间
	 */
	public Timestamp F05;
}
