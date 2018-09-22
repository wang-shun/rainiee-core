package com.dimeng.p2p.modules.account.console.service.entity;

import com.dimeng.p2p.S61.entities.T6102;
import com.dimeng.p2p.S61.enums.T6101_F03;

public class ZjDetailView extends T6102 {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户名
	 */
	public String userName;
	
	/**
	 * 类型明细
	 */
	public String tradingName;
	
	/**
	 * 账户类型
	 */
	public T6101_F03 zhlx;
	
	/**
	 * 关联用户
	 */
	public String assUserName; 


}
