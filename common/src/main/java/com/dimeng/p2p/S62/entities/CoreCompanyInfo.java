package com.dimeng.p2p.S62.entities
;

import com.dimeng.framework.service.AbstractEntity;

/** 
 * 核心企业信息 
 */
public class CoreCompanyInfo extends AbstractEntity{

	private static final long serialVersionUID = 1L;

	/**
	 * 自增ID
	 */
	public int id;
	/**
	 * 总行代码
	 */
	public String bankNo;

	/**
	 * 城市代码
	 */
	public String cityNo;
	/**
	 * 支行名称
	 */
	public String branchNm;
	/**
	 * 账号
	 */
	public String accntNo;
	/**
	 * 账户名称
	 */
	public String accntNm;

	public int bid;
	
	public int nounUserId;
	
    /** 
     * 核心企业对应用户id
     */
    public int userId;
    
    public int cardId;
	
}
