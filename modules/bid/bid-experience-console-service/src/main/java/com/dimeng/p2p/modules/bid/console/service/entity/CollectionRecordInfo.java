package com.dimeng.p2p.modules.bid.console.service.entity;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S71.entities.T7152;
import com.dimeng.p2p.common.enums.OverdueEnum;

/**
 * 催收记录信息
 * 
 * @author gongliang
 * 
 */
public class CollectionRecordInfo extends T7152 {
	private static final long serialVersionUID = 1L;

	/**
	 * 帐户名
	 */
	public String userName;
	/**
	 * 真实姓名
	 */
	public String realName;
	/**
	 * 手机
	 */
	public String phone;

	/**
	 * 是否逾期
	 */
	public OverdueEnum sfyq;
	/**
	 * 用户类型
	 */
	public T6110_F06 userType;
	/**
	 * 担保方
	 */
	public T6110_F10 dbf;
	
	/**
	 * 标编号
	 */
	public String bidNum;
}
