package com.dimeng.p2p.S50.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S50.enums.T5018_F03;

/**
 * 业绩报告
 */
public class T5018 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 业绩报告自增ID
	 */
	public int F01;

	/**
	 * 排序值
	 */
	public int F02;

	/**
	 * 是否发布,WFB:未发布;YFB:已发布
	 */
	public T5018_F03 F03;

	/**
	 * 浏览次数
	 */
	public int F04;

	/**
	 * 文章标题
	 */
	public String F05;

	/**
	 * 附件编码
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
