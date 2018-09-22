package com.dimeng.p2p.S60.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6013_F02;
import com.dimeng.p2p.S60.enums.T6013_F04;
import com.dimeng.p2p.S60.enums.T6013_F06;
import com.dimeng.p2p.S60.enums.T6013_F15;

/**
 * 用户工作信息
 */
public class T6013 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户帐号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 工作状态,GXJC:工薪阶层
	 */
	public T6013_F02 F02;

	/**
	 * 单位名称
	 */
	public String F03;

	/**
	 * 是否工作认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6013_F04 F04;

	/**
	 * 职位
	 */
	public String F05;

	/**
	 * 是否技术职称认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6013_F06 F06;

	/**
	 * 工作邮箱
	 */
	public String F07;

	/**
	 * 工作城市
	 */
	public String F08;

	/**
	 * 公司地址
	 */
	public String F09;

	/**
	 * 公司类别
	 */
	public String F10;

	/**
	 * 公司行业
	 */
	public String F11;

	/**
	 * 公司规模
	 */
	public String F12;

	/**
	 * 在现单位工作年限
	 */
	public String F13;

	/**
	 * 月收入
	 */
	public String F14;

	/**
	 * 是否有收入证明,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6013_F15 F15;

	/**
	 * 公司电话
	 */
	public String F16;

}
