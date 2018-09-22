package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S62.enums.T6230_F11;
import com.dimeng.p2p.S62.enums.T6230_F13;
import com.dimeng.p2p.S62.enums.T6230_F14;
import com.dimeng.p2p.S62.enums.T6252_F09;

public class BackOffList {

	/**
	 * 借款标ID
	 */
	public int jkbId;
	/**
	 * 债权编码
	 */
	public String assestsId;
	/**
	 * 借款人
	 */
	public String creditor;
	/**
	 * 类型详细
	 */
	public String typeDetail;
	/**
	 * 金额
	 */
	public BigDecimal money = new BigDecimal(0);
	/**
	 * 收款日期
	 */
	public Timestamp receiveDate;
	/**
	 * 标类型
	 */
	public int creditType;
	/**
	 * 回款状态
	 */
	public T6252_F09 dsStatus;
	/**
	 * 是否有担保
	 */
	public T6230_F11 F11;
	/**
	 * 是否有抵押
	 */
	public T6230_F13 F13;
	/**
	 * 是否实地认证
	 */
	public T6230_F14 F14;
    
    /**
     * 回收类型
     */
    public String backType;
}
