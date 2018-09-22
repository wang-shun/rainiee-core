/**
 * 
 */
package com.dimeng.p2p.S62.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6287_F07;

/**
 * 投资奖励发放记录表信息
 * @author guomianyun
 *
 */
public class T6287 extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 投资记录ID,参考T6250.F01
	 */
	public int F02;

	/**
	 * 标ID,参考T6230.F01
	 */
	public int F03;

	/**
	 * 发放人ID,参考T7110.F01
	 */
	public int F04 ;

	/**
	 * 发放金额
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 发放时间
	 */
	public Timestamp F06;

	/**
	 * 状态,DFF:待发放;YFF:已发放;BFF:不发放
	 */
	public T6287_F07 F07;
	
	/**
	 * 发放/未发放描述
	 */
	public String F08;

}
