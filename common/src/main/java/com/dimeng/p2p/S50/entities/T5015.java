package com.dimeng.p2p.S50.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S50.enums.T5015_F02;
import com.dimeng.p2p.S50.enums.T5015_F04;

/**
 * 网站公告
 */
public class T5015 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 网站公告自增ID
	 */
	public int F01;

	/**
	 * 类型,XT:系统;HD:活动
	 */
	public T5015_F02 F02;

	/**
	 * 浏览次数
	 */
	public int F03;

	/**
	 * 发布状态,WFB:未发布;YFB:已发布
	 */
	public T5015_F04 F04;

	/**
	 * 公告标题
	 */
	public String F05;

	/**
	 * 内容
	 */
	public String F06;

	/**
	 * 创建者ID,参考T7011.F01
	 */
	public int F07;

	/**
	 * 创建时间
	 */
	public Timestamp F08;

	/**
	 * 最后修改时间
	 */
	public Timestamp F09;

}
