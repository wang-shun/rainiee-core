package com.dimeng.p2p.modules.account.console.service.query;

import java.sql.Timestamp;


public abstract interface CzglRecordExtendsQuery extends CzglRecordQuery {
	/**
	 * 用户类型
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getUsersType();
	/**
	 * 订单号， 模糊查询
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getOrderNo();
	
	/**
	 * 支付公司代号
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getPayComName();

	/**
	 * 充值状态
	 * 
	 * @return {@link String}空值无效
	 */
	public abstract String getChargeStatus();
    
    /**
     * 充值完成时间， 大于等于查询
     * 
     * @return {@link Timestamp}空值无效
     */
    public abstract Timestamp getFinishStartRechargeTime();
    
    /**
     * 充值完成时间， 小于等于查询
     * 
     * @return {@link Timestamp}空值无效
     */
    public abstract Timestamp getFinishEndRechargeTime();

}
