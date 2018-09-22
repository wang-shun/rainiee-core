/*
 * 文 件 名:  T6288.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月14日
 */
package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6288_F06;
import com.dimeng.p2p.S62.enums.T6288_F07;
import com.dimeng.p2p.S62.enums.T6288_F08;

/**
 * 加息券投资记录 <功能详细描述>
 * 
 * @author xiaoqi
 * @version [v3.1.2, 2015年10月14日]
 */
public class T6288 extends AbstractEntity {
	private static final long serialVersionUID = 1463000390617518324L;

	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 标ID,参考T6230.F01
	 */
	public int F02;

	/**
	 * 投资人ID,参考T6110.F01
	 */
	public int F03;

	/**
	 * 加息率
	 */
	public BigDecimal F04;

	/**
	 * 投资时间
	 */
	public Timestamp F05;

	/**
	 * 是否取消,F:否;S:是;
	 */
	public T6288_F06 F06;

	/**
	 * 是否放款,F:否;S:是;
	 */
	public T6288_F07 F07;

	/**
	 * 是否自动投资
	 */
	public T6288_F08 F08;

	/**
	 * 投资记录ID,参考T6250.F01
	 */
	public int F09;

	/**
	 * 用户活动表id;参考T6342.F01
	 */
	public int F10;
	
	/**
     * 投资金额
     */
    public BigDecimal F11;
}
