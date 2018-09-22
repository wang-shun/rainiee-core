package com.dimeng.p2p.S60.entities;

import java.sql.Blob;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 用户认证附件
 */
public class T6019 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户认证附件自增ID
	 */
	public int F01;

	/**
	 * 用户认证信息ID,T6018.F01
	 */
	public int F02;

	/**
	 * 文件内容
	 */
	public Blob F03;

	/**
	 * 文件类型
	 */
	public String F04;

	/**
	 * 上传时间
	 */
	public Timestamp F05;

}
