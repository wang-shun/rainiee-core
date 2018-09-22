package com.dimeng.p2p.escrow.fuyou.entity;

/**
 * 用户信息查询后返回的实体
 * 
 * @author zhouchuanlong
 * @date 2014年9月11日
 */
public class UserQueryResponseEntity extends UserReviseResponseEntity {

	/**
     * 注释内容
     */
    private static final long serialVersionUID = 328060420042356082L;

    // 客户姓名
	private String custNm;

	// 身份证号码
	private String certifId;

	// 手机号码
	private String mobileNo;

	// 邮箱地址
	private String email;

	// 开户行地区代码
	private String cityId;

	// 开户行行别
	private String parentBankId;

	// 开户行支行名称
	private String bankNm;

	// 帐号
	private String capAcntNo;
	
	//签约状态
	private String cardPwdVerifySt;
	
	//账户信息验证状态
	private String idNmVerifySt;
	
	//账户生效状态
	private String contractSt;
	
	//用户状态
	private String userSt;

	public String getCust_nm() {
		return custNm;
	}

	public void setCust_nm(String cust_nm) {
		this.custNm = cust_nm;
	}

	public String getCertif_id() {
		return certifId;
	}

	public void setCertif_id(String certif_id) {
		this.certifId = certif_id;
	}

	public String getMobile_no() {
		return mobileNo;
	}

	public void setMobile_no(String mobile_no) {
		this.mobileNo = mobile_no;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCity_id() {
		return cityId;
	}

	public void setCity_id(String city_id) {
		this.cityId = city_id;
	}

	public String getParent_bank_id() {
		return parentBankId;
	}

	public void setParent_bank_id(String parent_bank_id) {
		this.parentBankId = parent_bank_id;
	}

	public String getBank_nm() {
		return bankNm;
	}

	public void setBank_nm(String bank_nm) {
		this.bankNm = bank_nm;
	}

	public String getCapAcntNo() {
		return capAcntNo;
	}

	public void setCapAcntNo(String capAcntNo) {
		this.capAcntNo = capAcntNo;
	}

	public String getCard_pwd_verify_st() {
		return cardPwdVerifySt;
	}

	public void setCard_pwd_verify_st(String card_pwd_verify_st) {
		this.cardPwdVerifySt = card_pwd_verify_st;
	}

	public String getId_nm_verify_st() {
		return idNmVerifySt;
	}

	public void setId_nm_verify_st(String id_nm_verify_st) {
		this.idNmVerifySt = id_nm_verify_st;
	}

	public String getContract_st() {
		return contractSt;
	}

	public void setContract_st(String contract_st) {
		this.contractSt = contract_st;
	}

	public String getUser_st() {
		return userSt;
	}

	public void setUser_st(String user_st) {
		this.userSt = user_st;
	}

}
