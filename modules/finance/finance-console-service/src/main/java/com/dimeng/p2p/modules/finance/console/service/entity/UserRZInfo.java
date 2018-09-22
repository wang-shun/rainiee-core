package com.dimeng.p2p.modules.finance.console.service.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import com.dimeng.p2p.common.enums.AttestationState;
import com.dimeng.p2p.common.enums.AttestationType;
/**
 * 用户认证信息
 * @author gaoshaolong
 *
 */
public class UserRZInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -140673682173231803L;
	
	/**
	 * 审核项目
	 */
	public AttestationType type;
	/**
	 * 认证状态
	 */
	public AttestationState rzStatus;
	/**
	 * 认证时间
	 */
	public Timestamp rzDate;

}
