package com.dimeng.p2p.modules.bid.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S62.entities.T6230;

/**
 * 企业借款协议
 * @author gaoshaolong
 *
 */
public class Qyjkxy extends T6230{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4920136743069525767L;
	/**
	 * 借款ID
	 */
	public int loanId;
	/**
	 * 协议编号
	 */
	public String xyNo;
	/**
	 * 协议版本号
	 */
	public int version;
	/** 
     * 协议类型ID,参考T5125.F01
     */
    public int xyId;
	/**
	 * 协议名称
	 */
	public String xyName;
	/**
	 * 借款者用户名
	 */
	public String jkLoginName;
	/**
	 * 企业名称
	 */
	public String qyName;
	/**
	 * 时间
	 */
	public Timestamp time;
}
