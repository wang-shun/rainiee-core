package com.dimeng.p2p.modules.bid.console.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6273;

/**
 * 企业借款协议
 * @author gaoshaolong
 *
 */
public class Dzqm extends T6273{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4920136743069525767L;
	/**
	 * 用户名
	 */
	public String loginName;
	/**
	 * 姓名、企业名称
	 */
	public String name;
	/**
	 * 客户编号
	 */
	public String userCode;
	/**
	 * 身份证号
	 */
	public String sfzh;
	/**
	 * 手机号码
	 */
	public String phone;
	/**
	 * 标编号
	 */
	public String bidCode;
	/**
	 * 合同标题
	 */
	public String htTitle;
	/**
	 * 交易号
	 */
	public String tradeCode;
	/**
	 * 请求时间
	 */
	public Timestamp submitDate;
	/**
	 * 用户id
	 */
	public int userId;
	/**
	 * 标的id
	 */
	public int bidId;
	
	/**
	 * 查看合同地址
	 */
	public String docUrl;
	
	/**
	 * 签名记录id
	 */
	public int qmId;
}
