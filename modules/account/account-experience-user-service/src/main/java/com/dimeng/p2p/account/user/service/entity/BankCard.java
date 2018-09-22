package com.dimeng.p2p.account.user.service.entity;

/**
 * 银行卡信息
 * @author Administrator
 *
 */
public class BankCard
{
    
    /**
     * id
     */
    public int id;
    
    /**
     * 用户ID
     */
    public int acount;
    
    /**
     * 银行ID
     */
    public int BankID;
    
    /**
     * 银行名称
     */
    public String Bankname;
    
    /**
     * 开户行地址
     */
    public String City;
    
    /**
     * 开户行名称
     */
    public String BankKhhName;
    
    /**
     * 银行卡号
     */
    public String BankNumber;
    
    /**
     * 银行卡状态
     */
    public String status;
    
    /**
     * 开户名
     */
    public String userName;
    
    /**
     * 开户类型（区分是个人还是企业）
     */
    public int type;
    
    /**
     * 解绑流水号
     */
    public String jbRequestNo;
    
    /**
    * 绑定标识号
    */
    public String bindId;
    
    /**
     * 银行卡认证状态
     */
    public String rzzt;

	public String getRzzt() {
		return rzzt;
	}

	public void setRzzt(String rzzt) {
		this.rzzt = rzzt;
	}
}
