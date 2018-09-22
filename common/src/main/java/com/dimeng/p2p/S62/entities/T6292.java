package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6292_F06;
import com.dimeng.p2p.S62.enums.T6292_F07;
import com.dimeng.p2p.S62.enums.T6292_F08;

/**
 * 红包投资记录
 */
public class T6292 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

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
	 * 红包金额
	 */
	public BigDecimal F04 = BigDecimal.ZERO;

	/**
	 * 投资时间
	 */
	public Timestamp F05;

	/**
	 * 是否取消,F:否;S:是;
	 */
	public T6292_F06 F06;

	/**
	 * 是否已放款,F:否;S:是;
	 */
	public T6292_F07 F07;
	
	/**
	 * 是否属于融资担保,F:否;S:是;
	 */
	public T6292_F08 F08;

	/**
	 * 投资记录ID,参考T6250.F01
	 */
	public int F09;
	
	 /**
     * 红包id;参考T6340.F01
     */
    public int F10;
}
