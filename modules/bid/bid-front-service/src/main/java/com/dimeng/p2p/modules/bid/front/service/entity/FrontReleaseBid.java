/**
 * 
 */
package com.dimeng.p2p.modules.bid.front.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * @author dimeng10
 *
 */
public class FrontReleaseBid extends AbstractEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1937306349141413644L;
	
	/**
	 *标的ID
	 */
	public int F01;
	/**
	 * 标的标题
	 */
	public String F02;
	
	/** 
     * 借款用户ID,参考T6110.F01
     */
	public int F03;
	
	/**
	 * 标用户登录账号
	 */
	public String F04;
	
	/** 
     * 发布时间,预发布状态有效
     */
    public Timestamp F05;
	
	 /** 
     * 借款金额
     */
    public BigDecimal F06 = new BigDecimal(0);
    /**
     * 可投金额
     */
    public BigDecimal F07 = new BigDecimal(0);

}
