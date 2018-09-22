/**
 * 
 */
package com.dimeng.p2p.modules.base.console.service.query;

import com.dimeng.p2p.S50.enums.T5019_F11;
import com.dimeng.p2p.S50.enums.T5019_F13;

/**
 * @author guopeng
 * 
 */
public interface RegionQuery {
	/**
	 * 根据类型查询
	 * 
	 * @return
	 */
	public abstract T5019_F11 getType();

	/**
	 * 根据名称查询
	 * 
	 * @return
	 */
	public abstract String getName();

	/**
	 * 根据启用状态查询
	 * 
	 * @return
	 */
	public abstract T5019_F13 getStatus();
}
