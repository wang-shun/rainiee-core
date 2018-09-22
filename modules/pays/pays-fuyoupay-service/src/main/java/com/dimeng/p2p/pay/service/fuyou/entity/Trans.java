package com.dimeng.p2p.pay.service.fuyou.entity;


import javax.xml.bind.annotation.XmlRootElement;

/**
 * 代付响应详细信息实体类
 * @author raoyujun
 *
 */
@XmlRootElement(name = "trans")
public class Trans {
	
	private String merdt;  // 原请求日期
	private String orderno;// 原请求流水数字串，当天必须唯一
	private String accntno;// 账号
	private String accntnm;// 账户名称
	private String amt;    // 交易金额
	private String entseq; // 企业流水号 是填写后，系统体现在交易查询和外部通知中
	private String memo;   // 备注 是填写后，系统体现在交易查询和外部通知中
	private String state;  // 交易状态
	private String result; // 交易结果
	private String reason; // 结果原因
	public String getMerdt() {
		return merdt;
	}
	public void setMerdt(String merdt) {
		this.merdt = merdt;
	}
	public String getOrderno() {
		return orderno;
	}
	public void setOrderno(String orderno) {
		this.orderno = orderno;
	}
	public String getAccntno() {
		return accntno;
	}
	public void setAccntno(String accntno) {
		this.accntno = accntno;
	}
	public String getAccntnm() {
		return accntnm;
	}
	public void setAccntnm(String accntnm) {
		this.accntnm = accntnm;
	}
	public String getAmt() {
		return amt;
	}
	public void setAmt(String amt) {
		this.amt = amt;
	}
	public String getEntseq() {
		return entseq;
	}
	public void setEntseq(String entseq) {
		this.entseq = entseq;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	
}
