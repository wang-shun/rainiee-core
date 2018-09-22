package com.dimeng.p2p.S60.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6012_F02;
import com.dimeng.p2p.S60.enums.T6012_F03;
import com.dimeng.p2p.S60.enums.T6012_F04;

/**
 * 用户家庭信息
 */
public class T6012 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户帐号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 婚姻状况,YH:已婚;WH:未婚
	 */
	public T6012_F02 F02;

	/**
	 * 有无子女,Y:有;W:无
	 */
	public T6012_F03 F03;

	/**
	 * 结婚认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6012_F04 F04;

	/**
	 * 直系亲属姓名
	 */
	public String F05;

	/**
	 * 关系
	 */
	public String F06;

	/**
	 * 手机
	 */
	public String F07;

	/**
	 * 其他联系人姓名
	 */
	public String F08;

	/**
	 * 关系
	 */
	public String F09;

	/**
	 * 手机
	 */
	public String F10;

}
