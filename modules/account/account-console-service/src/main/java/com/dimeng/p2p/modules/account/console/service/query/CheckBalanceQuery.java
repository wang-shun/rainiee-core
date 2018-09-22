/*
 * 文 件 名:  CheckBalanceQuery.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月20日
 */
package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [版本号, 2015年11月20日]
 */
public abstract interface CheckBalanceQuery {

	public abstract String getOrderId();
	
	public abstract String getType();
	
	public abstract String getOperationer();
	
	public abstract Timestamp getOptStart();
	
	public abstract Timestamp getOptEnd();
	
	public abstract String getStatus();
}
