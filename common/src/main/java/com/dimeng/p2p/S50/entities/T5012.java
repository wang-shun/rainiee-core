package com.dimeng.p2p.S50.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S50.enums.T5012_F03;
import com.dimeng.p2p.S50.enums.T5012_F11;

/**
 * 在线客服
 */
public class T5012 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 在线客服自增ID
	 */
	public int F01;

	/**
	 * 浏览次数
	 */
	public int F02;

	/**
	 * 客服类型,QQ:QQ;DH:电话号码;YX:邮箱
	 */
	public T5012_F03 F03;

	/**
	 * 排序值
	 */
	public int F04;

	/**
	 * 客服名称
	 */
	public String F05;

	/**
	 * 客服号码
	 */
	public String F06;

	/**
	 * 图片编码
	 */
	public String F07;

	/**
	 * 创建者,参考T7011.F01
	 */
	public int F08;

	/**
	 * 创建时间
	 */
	public Timestamp F09;

	/**
	 * 最后更新时间
	 */
	public Timestamp F10;

	/**
	 * 客服状态
	 */
	public T5012_F11 F11;

}
