package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6020_F03;

/**
 * 用户认证记录
 */
public class T6020 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户认证记录自增ID
	 */
	public int F01;

	/**
	 * 用户帐号ID,参考T6010.F01
	 */
	public int F02;

	/**
	 * 附件类别,SFRZ:身份认证;SJSMRZ:手机实名认证;XLRZ:学历认证;JZDZM:居住地证明;JHRZ;结婚认证:GZRZ:工作认证;
	 * JSZCRZ
	 * :技术职称认证;SRZM:收入证明;XYBG:信用报告;FCRZ:房产认证;GCRZ:购车证明;WBRZ:微博认证;GSRZ:公司认证;
	 */
	public T6020_F03 F03;

	/**
	 * 认证人员ID,参考T7011.F01
	 */
	public int F04;

	/**
	 * 认证时间
	 */
	public Timestamp F05;

	/**
	 * 认证结果
	 */
	public String F06;

	/**
	 * 认证批号
	 */
	public String F07;

}
