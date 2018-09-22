package com.dimeng.p2p.S70.entities;

import java.sql.Blob;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S70.enums.T7100_F03;

/**
 * 实名认证信息库
 */
public class T7100 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 身份证号
	 */
	public String F01;

	/**
	 * 姓名
	 */
	public String F02;

	/**
	 * 认证状态,TG:通过;SB:失败;
	 */
	public T7100_F03 F03;

	/**
	 * 图像
	 */
	public Blob F04;

	/**
	 * 最后更新日期
	 */
	public Timestamp F05;

}
