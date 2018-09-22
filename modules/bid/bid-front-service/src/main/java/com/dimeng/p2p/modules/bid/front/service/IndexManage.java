/**
 * 
 */
package com.dimeng.p2p.modules.bid.front.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;

/**
 * @author guopeng
 * 
 */
public interface IndexManage extends Service {

	public abstract int getUserCount() throws Throwable;

	public abstract BigDecimal getTzAmount() throws Throwable;
}
