package com.dimeng.p2p.modules.bid.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S71.enums.T7152_F04;


/**
 * 催收信息
 * @author gongliang
 *
 */
public abstract interface StayRefund {
	
	/**
	 * 借款Id.
	 * 
	 * @return {@code int}空值无效.
	 */
	public abstract int getLoanRecordId();
	
	/**
	 * 帐户Id.
	 * 
	 * @return {@code int}空值无效.
	 */
	public abstract int getUserId();
	
	/**
	 * 催收方式
	 * 
	 * @return {@code T7152_F04}空值无效.
	 */
	public abstract T7152_F04 getCollectionType();
	
	/**
	 * 催收人
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getCollectionPerson();
	
	/**
	 * 催收时间
	 * 
	 * @return {@code Timestamp}空值无效.
	 */
	public abstract Timestamp getCollectionTime();
	
	/**
	 * 结果概要
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getResultDesc();
	
	/**
	 * 备注
	 * 
	 * @return {@code String}空值无效.
	 */
	public abstract String getComment();
}
