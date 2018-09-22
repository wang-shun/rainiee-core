package com.dimeng.p2p.modules.finance.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.ChargeStatus;

public class CzglRecord {
	
	
	/** 
	 * id
	 */
	public int id;	

	/** 
	 * 用户名
	 */
	public String loginName;
	
	/** 
	 * 姓名
	 */
	public String userName;
	
	/** 
	 * 充值金额
	 */
	public BigDecimal userAmount=new BigDecimal(0);
	
	/** 
	 * 手续费
	 */
	public BigDecimal charge=new BigDecimal(0);
	
	/** 
	 * 流水号
	 */
	public String singleNumber;
	
	/** 
	 * 充值方式
	 */
	public String payType;
	
	/** 
	 * 充值状态
	 */
	public ChargeStatus rechargeStatus;
	/** 
	 * 充值时间
	 */
	public Timestamp chargeDateTime;

}
