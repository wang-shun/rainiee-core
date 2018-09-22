/**
 * 
 */
package com.dimeng.p2p.modules.bid.console.service.entity;

/**
 * 投资、流标、放款返回对象
 * 
 * @author guopeng
 * 
 */
public class BidReturn {
	/**
	 * 投资返回订单ID
	 */
	public int[] bidOrderIds;

	/**
	 * 体验金投资返回ID
	 */
	public String experOrderIds;

	/**
	 * 加息券放款订单ID
	 */
	public String couponOrderIds;
}