package com.dimeng.p2p.S60.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6014_F02;
import com.dimeng.p2p.S60.enums.T6014_F04;
import com.dimeng.p2p.S60.enums.T6014_F12;
import com.dimeng.p2p.S60.enums.T6014_F14;

/**
 * 用户公司信息
 */
public class T6014 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户帐号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 职业状态,SYQYZ:私营企业主
	 */
	public T6014_F02 F02;

	/**
	 * 公司名称
	 */
	public String F03;

	/**
	 * 是否公司认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6014_F04 F04;

	/**
	 * 公司所在城市
	 */
	public String F05;

	/**
	 * 公司地址
	 */
	public String F06;

	/**
	 * 公司类别
	 */
	public String F07;

	/**
	 * 公司所行业
	 */
	public String F08;

	/**
	 * 公司规模
	 */
	public String F09;

	/**
	 * 公司经营年限
	 */
	public String F10;

	/**
	 * 公司月收入
	 */
	public String F11;

	/**
	 * 是否有收入证明,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6014_F12 F12;

	/**
	 * 职位
	 */
	public String F13;

	/**
	 * 是否有技术职称认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6014_F14 F14;

	/**
	 * 公司电话
	 */
	public String F15;

	/**
	 * 公司邮箱
	 */
	public String F16;

}
