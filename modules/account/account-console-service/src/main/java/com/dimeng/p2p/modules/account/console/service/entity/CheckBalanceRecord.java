/*
 * 文 件 名:  CheckBalanceRecord.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月20日
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6104_F07;

/**
 * 平台调账管理
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年11月20日]
 */
public class CheckBalanceRecord extends AbstractEntity {

	private static final long serialVersionUID = 3255652972211769894L;

	/**
	 * 订单号
	 */
	public String orderId;
	
	/**
	 * 收入
	 */
	public BigDecimal income;
	
	/**
	 * 支出
	 */
	public BigDecimal expend;
	
	/**
	 * 类型
	 */
	public String type;
	
	/**
	 * 备注
	 */
	public String remark;
	
	/**
	 * 操作人
	 */
	public String operationer;
	
	/**
	 * 状态
	 */
	public T6104_F07 status;
	
	/**
	 * 操作时间
	 */
	public Timestamp operationTime;
}
