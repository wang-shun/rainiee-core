package com.dimeng.p2p.S62.entities;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6282_F11;
import com.dimeng.p2p.S62.enums.T6282_F15;
import com.dimeng.p2p.S62.enums.T6282_F16;
import com.dimeng.p2p.S62.enums.T6282_F17;
import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * 个人融资申请
 */
public class T6282 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 用户ID，参考T6110.F01
	 */
	public int F02;

	/**
	 * 联系人
	 */
	public String F03;

	/**
	 * 联系电话
	 */
	public String F04;

	/**
	 * 身份证
	 */
	public String F05;

	/**
	 * 融资金额
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 借款类型ID,参考T6211.F01
	 */
	public int F07;

	/**
	 * 所在区域ID，参考T5019.F01
	 */
	public int F08;

	/**
	 * 预计筹款期限
	 */
	public String F09;

	/**
	 * 借款描述
	 */
	public String F10;

	/**
	 * 处理状态,WCL:未处理;YCL:已处理
	 */
	public T6282_F11 F11;

	/**
	 * 处理人,参考T7110.F01
	 */
	public int F12;

	/**
	 * 申请时间
	 */
	public Timestamp F13;

	/**
	 * 处理时间
	 */
	public Timestamp F14;

	/**
	 * 是否有抵押,S:是;F:否;
	 */
	public T6282_F15 F15;

	/**
	 * 是否实地认证,S:是;F:否;
	 */
	public T6282_F16 F16;

	/**
	 * 是否有担保,S:是;F:否;
	 */
	public T6282_F17 F17;

	/**
	 * 处理结果
	 */
	public String F18;
	
	/**
	 * 借款期限，单位月
	 */
	public int F19;

}
