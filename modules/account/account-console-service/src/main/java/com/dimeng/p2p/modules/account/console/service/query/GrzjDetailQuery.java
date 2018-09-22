package com.dimeng.p2p.modules.account.console.service.query;

import java.util.Date;

import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;


public abstract interface GrzjDetailQuery {
	/**
	 * 用户名， 模糊查询
	 * @return {@link String}空值无效
	 */
	public abstract String getLoginName();
	
	/**
	 * 类型明细
	 * @return {@link String}空值无效
	 */
	public abstract int getTradingType();
	
	/**
	 * 开始时间
	 * @return {@link String}空值无效
	 */
	public abstract Date getStartDate();
	
	/**
	 * 结束时间
	 * @return {@link String}空值无效
	 */
	public abstract Date getEndDate();
	
	/**
	 * 用户类型
	 * @return {@link String}空值无效
	 */
	public abstract T6110_F06 getUserType();
	
	/**
	 * 用户是否担保方
	 * @return {@link String}空值无效
	 */
	public abstract T6110_F10 getUserDb();
	
	/**
	 * 是否查询平台明细
	 * @return true,false
	 */
	public abstract boolean getPlatDetailFlg();
	
	/**
	 * 账户类型
	 * @return
	 */
	public abstract T6101_F03 getZhlx();
}
