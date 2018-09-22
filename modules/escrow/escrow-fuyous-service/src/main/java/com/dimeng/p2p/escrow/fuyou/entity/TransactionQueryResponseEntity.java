package com.dimeng.p2p.escrow.fuyou.entity;

import java.util.List;

/**
 * 
 * 交易查询接口接收实本类
 * 
 * @author  heshiping
 * @version  [版本号, 2015年5月25日]
 */
public class TransactionQueryResponseEntity {
	// 响应码
	public String respCode;
	// 商户代码
	public String mchntCd;
	// 流水号
	public String mchntTxnSsn;
	// 业务类型	busi_tp
	public String  busiTp;
	// 总记录
	public int totalNumber;
	// 明细实体类
	public List<TransactionQueryDetailedEntity>  detailedEntity;
	// 签名数据
	public String signature;
}
