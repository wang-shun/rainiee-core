package com.dimeng.p2p.account.front.service.entity;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6120_F05;
/**
 * 用户认证信息
 *
 */
public class UserRZInfo  extends AbstractEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = -140673682173231803L;
	
	/** 
     * 认证状态,WYZ:未验证;TG:通过;BTG:不通过
     */
    public T6120_F05 F01;

    /** 
     * 认证时间
     */
    public Timestamp F02;

    /** 
     * 类型名称
     */
    public String F03;

}
