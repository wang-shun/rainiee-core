package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6017_F02;
import com.dimeng.p2p.S60.enums.T6017_F04;

/**
 * 用户认证状态表
 */
public class T6017 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户帐号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 附件类别,SFRZ:身份认证;SJSMRZ:手机实名认证;XLRZ:学历认证;JZDZM:居住地证明;JHRZ;结婚认证:GZRZ:工作认证;
	 * JSZCRZ
	 * :技术职称认证;SRZM:收入证明;XYBG:信用报告;FCRZ:房产认证;GCRZ:购车证明;WBRZ:微博认证;GSRZ:公司认证;
	 * JBXXRZ:基本信息认证
	 */
	public T6017_F02 F02;

	/**
	 * 用户认证信息ID,T6018.F01
	 */
	public int F03;

	/**
	 * 认证状态,WYZ:未验证;DSH:待审核;YYZ:已验证;WTG:未通过
	 */
	public T6017_F04 F04;

	/**
	 * 过期时间
	 */
	public Timestamp F05;

	/**
	 * 信用分数
	 */
	public int F06;

}
