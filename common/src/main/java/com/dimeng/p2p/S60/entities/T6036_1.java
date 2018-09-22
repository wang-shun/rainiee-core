package com.dimeng.p2p.S60.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6036_1_F02;
import com.dimeng.p2p.S60.enums.T6036_1_F04;
import com.dimeng.p2p.S60.enums.T6036_1_F05;

/**
 * 借款标扩展信息
 */
public class T6036_1 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 借款标ID,参考T6036.F01
	 */
	public int F01;

	/**
	 * GXJC:工薪阶层;SYQYZ:私营企业主
	 */
	public T6036_1_F02 F02;

	/**
	 * 封面图片
	 */
	public String F03;

	/**
	 * 是否推荐,F:否,S:是
	 */
	public T6036_1_F04 F04;

	/**
	 * 是否线上（是，否）
	 */
	public T6036_1_F05 F05;

	/**
	 * 项目所在区域ID(参考 T5019.F01
	 */
	public int F06;

}
