package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6216_F03;
import com.dimeng.p2p.S60.enums.T6216_F05;

/**
 * 标的附件表
 */
public class T6216 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 借款标ID
	 */
	public int F02;

	/**
	 * 附件类型（协议，房产，相关证照，其他）
	 */
	public T6216_F03 F03;

	/**
	 * 图片编码
	 */
	public String F04;

	/**
	 * 是否马赛克（是否）
	 */
	public T6216_F05 F05;

	/**
	 * 上传时间
	 */
	public Timestamp F06;

	/**
	 * 附件格式
	 */
	public String F07;

	/**
	 * 附件名称
	 */
	public String F08;

	/**
	 * 上传人(参考T7011.F01)
	 */
	public int F09;

	/**
	 * 文件大小
	 */
	public int F10;

}
