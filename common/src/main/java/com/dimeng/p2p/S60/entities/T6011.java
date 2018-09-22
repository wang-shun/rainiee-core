package com.dimeng.p2p.S60.entities;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6011_F03;
import com.dimeng.p2p.S60.enums.T6011_F05;
import com.dimeng.p2p.S60.enums.T6011_F08;
import com.dimeng.p2p.S60.enums.T6011_F09;
import com.dimeng.p2p.S60.enums.T6011_F15;
import com.dimeng.p2p.S60.enums.T6011_F16;
import com.dimeng.p2p.S60.enums.T6011_F18;
import com.dimeng.p2p.S60.enums.T6011_F25;
import com.dimeng.p2p.S60.enums.T6011_F28;

/**
 * 用户基本信息
 */
public class T6011 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户账号ID,参考T6010.F01
	 */
	public int F01;

	/**
	 * 手机号
	 */
	public String F02;

	/**
	 * 手机是否实名验证,WYZ:未验证;YYZ:已验证(通过);BH:驳回;
	 */
	public T6011_F03 F03;

	/**
	 * 邮箱
	 */
	public String F04;

	/**
	 * 邮箱是否验证,WYZ:未验证(审核中);YYZ:已验证(通过);
	 */
	public T6011_F05 F05;

	/**
	 * 姓名
	 */
	public String F06;

	/**
	 * 身份证号
	 */
	public String F07;

	/**
	 * 实名是否验证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6011_F08 F08;

	/**
	 * 身份证是否认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6011_F09 F09;

	/**
	 * 邀请码
	 */
	public String F10;

	/**
	 * 我的邀请码
	 */
	public String F11;

	/**
	 * 注册时间
	 */
	public Timestamp F12;

	/**
	 * 最后登录时间
	 */
	public Timestamp F13;

	/**
	 * 后登录IP
	 */
	public String F14;

	/**
	 * 兴趣类型,LC:理财;JK:借款
	 */
	public T6011_F15 F15;

	/**
	 * 性别,F:女;M:男;QT:其他
	 */
	public T6011_F16 F16;

	/**
	 * 最高学历
	 */
	public String F17;

	/**
	 * 学历是否认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6011_F18 F18;

	/**
	 * 学历12位验证码
	 */
	public String F19;

	/**
	 * 入学年份
	 */
	public int F20;

	/**
	 * 毕业院校
	 */
	public String F21;

	/**
	 * 籍贯
	 */
	public String F22;

	/**
	 * 户口所在地
	 */
	public String F23;

	/**
	 * 居住地址
	 */
	public String F24;

	/**
	 * 居住地是否认证,WYZ:未验证(审核中);YYZ:已验证(通过);BH:驳回
	 */
	public T6011_F25 F25;

	/**
	 * 居住地邮编
	 */
	public String F26;

	/**
	 * 居住地电话
	 */
	public String F27;

	/**
	 * 是否拉黑,S:是;F:否
	 */
	public T6011_F28 F28;

	/**
	 * 用户头像Code
	 */
	public String F29;

	/**
	 * 实名认证次数
	 */
	public short F30;

	/**
	 * 邮件认证次数
	 */
	public short F31;

	/**
	 * 短信认证次数
	 */
	public short F32;

}
