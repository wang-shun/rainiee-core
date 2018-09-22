package com.dimeng.p2p.S70.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7021_F03;
import com.dimeng.p2p.S70.enums.T7021_F08;

/**
 * 皮肤管理
 */
public class T7021 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 皮肤管理自增ID
	 */
	public int F01;

	/**
	 * 主题
	 */
	public String F02;

	/**
	 * 位置,SY:首页;GRZX:个人中心
	 */
	public T7021_F03 F03;

	/**
	 * 图片Code
	 */
	public String F04;

	/**
	 * 创建者,参考T7011.F01
	 */
	public int F05;

	/**
	 * 创建时间
	 */
	public Timestamp F06;

	/**
	 * 最后修改时间
	 */
	public Timestamp F07;

	/**
	 * 是否生效,S:是;F:否
	 */
	public T7021_F08 F08;

}
