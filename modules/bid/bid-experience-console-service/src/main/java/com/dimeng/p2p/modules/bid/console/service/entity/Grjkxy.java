package com.dimeng.p2p.modules.bid.console.service.entity;

import java.sql.Timestamp;

/**
 * 个人借款协议
 * @author gaoshaolong
 *
 */
public class Grjkxy {
	/**
	 * 借款ID
	 */
	public int loanId;
	/**
	 * 协议编号
	 */
	public String xyNo;
	/**
	 * 协议名称
	 */
	public String xyName;
	/**
	 * 借款者用户名
	 */
	public String jkLoginName;
	/**
	 * 借款者姓名
	 */
	public String qyName;
	/**
	 * 身份证
	 */
	public String cardNo;
	/**
	 * 签订时间
	 */
	public Timestamp time;
}
