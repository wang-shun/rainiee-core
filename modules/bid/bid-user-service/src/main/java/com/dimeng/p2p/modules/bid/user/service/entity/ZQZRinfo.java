package com.dimeng.p2p.modules.bid.user.service.entity;

import java.io.Serializable;

/**
 * 债权转让详细信息
 * 
 * @author chizijia
 * 
 *         2016-12-19
 * 
 */
public class ZQZRinfo implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 债权人Id
	 */
	public int zqrId;

	/**
	 * 受让人Id
	 */
	public int srrId;
	/**
	 * 标的id
	 */
	 public int bidId;
	 /**
	  * 债权id
	  */
	 public int zqzrId;
	 
	 /**
	  * 标编号
	  */
	 public String bidCode;
	 
	 /**
	  * 债权申请id
	  */
	 public int zqsqId;
	 
	 /**
	  * 投资记录id
	  */
	 public int recordId;

}
