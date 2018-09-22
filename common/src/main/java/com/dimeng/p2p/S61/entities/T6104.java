/*
 * 文 件 名:  T6104.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月22日
 */
package com.dimeng.p2p.S61.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6104_F07;

/**
 * 平台调账信息实体
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年11月22日]
 */
public class T6104 extends AbstractEntity {

	/**
	 * 注释内容
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int F01;
	
	/**
	 * 平台账号ID,参考T6101.F01
	 */
	public int F02;
	
	/**
	 * 订单id,参考S65.T6501 F01
	 */
	public int F03;
	
	/**
	 * 交易类型ID,参考T5122.F01
	 */
	public int F04;
	
	/**
	 * 收入
	 */
	public BigDecimal F05;
	/**
	 * 支出
	 */
	public BigDecimal F06;
	
	/**
	 * 交易状态：DTJ:待提交,ZFCG:支付成功,ZFSB:支付失败,WZF:未支付,TXCG:提现成功,TXSB:提现失败
	 */
	public T6104_F07 F07;
	
	/**
	 * 创建时间
	 */
	public Timestamp F08;
	
	/**
	 * 操作人，参考 S71.T7110 F01
	 */
	public int F09;
	
	/**
	 * 备注
	 */
	public String F10;
}
