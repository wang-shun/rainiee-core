package com.dimeng.p2p.S60.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S60.enums.T6039_F11;
import com.dimeng.p2p.S60.enums.T6039_F12;

/**
 * 债权转让-转出表
 */
public class T6039 extends AbstractEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 债权转让-转出表自增ID
	 */
	public int F01;

	/**
	 * 债权记录ID,参考T6038.F01
	 */
	public int F02;

	/**
	 * 借款标ID,参考T6036.F01
	 */
	public int F03;

	/**
	 * 转出者ID,参考T6010.F01
	 */
	public int F04;

	/**
	 * 债权价值
	 */
	public BigDecimal F05 = BigDecimal.ZERO;

	/**
	 * 转让价格
	 */
	public BigDecimal F06 = BigDecimal.ZERO;

	/**
	 * 转出份数
	 */
	public int F07;

	/**
	 * 剩余份数
	 */
	public int F08;

	/**
	 * 创建时间
	 */
	public Timestamp F09;

	/**
	 * 最后更新时间
	 */
	public Timestamp F10;

	/**
	 * 转让状态,YX:有效;WX:无效
	 */
	public T6039_F11 F11;

	/**
	 * 是否发布,S:是;F:否
	 */
	public T6039_F12 F12;

	/**
	 * 预计收益
	 */
	public BigDecimal F13 = BigDecimal.ZERO;

}
