package com.dimeng.p2p.modules.base.console.service.entity;

import com.dimeng.p2p.S51.enums.T5122_F03;

/**
 * 交易类型
 * 
 */
public interface TradetypeQuery {
	/**
	 * 名称
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * 状态,QY:启用;TY:停用
	 * 
	 * @return
	 */
	public T5122_F03 getStatus();
}
