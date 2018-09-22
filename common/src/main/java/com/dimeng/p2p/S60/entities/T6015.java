package com.dimeng.p2p.S60.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6015_F02;
import com.dimeng.p2p.S60.enums.T6015_F03;
import com.dimeng.p2p.S60.enums.T6015_F04;
import com.dimeng.p2p.S60.enums.T6015_F05;
import com.dimeng.p2p.S60.enums.T6015_F06;
import com.dimeng.p2p.S60.enums.T6015_F07;
import com.dimeng.p2p.S60.enums.T6015_F08;

/**
 * 用户资产信息
 */
public class T6015 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户帐号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 是否信用报告认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6015_F02 F02;

	/**
	 * 是否有房产,Y:有;W:无
	 */
	public T6015_F03 F03;

	/**
	 * 有无房贷,Y:有;W:无
	 */
	public T6015_F04 F04;

	/**
	 * 是否房产认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6015_F05 F05;

	/**
	 * 是否有车,Y:有;W:无
	 */
	public T6015_F06 F06;

	/**
	 * 是否购车认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6015_F07 F07;

	/**
	 * 是否有车贷,Y:有;W:无
	 */
	public T6015_F08 F08;

	/**
	 * 购车年份
	 */
	public int F09;

	/**
	 * 汽车品牌
	 */
	public String F10;

	/**
	 * 车牌号码
	 */
	public String F11;

}
