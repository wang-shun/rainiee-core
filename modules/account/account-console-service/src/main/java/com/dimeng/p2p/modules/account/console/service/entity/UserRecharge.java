/**
 * 
 */
package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S65.entities.T6502;

public class UserRecharge extends T6502 {

	private static final long serialVersionUID = 1L;

	/**
	 * 用户登录账号
	 */
	public String userName;
	/**
	 * 用户类型
	 */
	public T6110_F06 userType;
	/**
	 * 充值时间 
	 */
	public Timestamp createTime;
	    
    /**
     * 充值完成时间 
     */
    public Timestamp chargeFinishTime;
    
    /**
     * 用户姓名
     */
	public String userRealName;
	/**
	 * 是否担保
	 */
	public T6110_F10 t6110_F10;
	/**
	 * 支付公司名称
	 */
	public String payComName;
	/**
	 * 充值总金额
	 */
	public BigDecimal countChargeAmount = BigDecimal.ZERO;
	/**
	 * 应收手续费总额
	 */
	public BigDecimal countReceivableAmount = BigDecimal.ZERO;
	/**
	 * 实收手续费总额
	 */
	public BigDecimal countPaidAmount = BigDecimal.ZERO;

}
