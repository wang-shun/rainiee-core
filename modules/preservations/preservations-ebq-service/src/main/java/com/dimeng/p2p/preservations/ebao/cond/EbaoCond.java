package com.dimeng.p2p.preservations.ebao.cond;

import org.mapu.themis.api.common.PersonalIdentifer;

/**
 * 
 * 易保全条件
 * <创建保全>
 * 
 * @author  dengwenwu
 * @version  [版本号, 2016年6月22日]
 */
public class EbaoCond
{
	/**
	 * 合同保全表主键id
	 */
	private int F01;

    /**
     * 文件路径
     */
    private String withFile;
    
    /**
     * 保全标题
     */
    private String withPreservationTitle;
    
    /**
     * 保全主体身份信息
     */
    private PersonalIdentifer withIdentifer;
    
    /**
     * 来源企业用户ID(和保全主体对应)
     */
    private String withSourceRegistryId;
    
    /**
     * 合同金额[格式：200000.00]
     */
    private double withContractAmount;
    
    /**
     * 合同编号
     */
    private String withContractNumber;
    
    /**
     * 用户邮箱（和电话二选一）
     */
    private String withUserEmail;
    
    /**
     * 用户电话（和邮箱二选一）
     */
    private String withPhoneNo;
    
    /**
     * 保全备注信息
     */
    private String withComments;

	public EbaoCond() {
		super();
	}

	public EbaoCond(int f01, String withFile, String withPreservationTitle,
			PersonalIdentifer withIdentifer, String withSourceRegistryId,
			double withContractAmount, String withContractNumber,
			String withUserEmail, String withPhoneNo, String withComments) {
		super();
		F01 = f01;
		this.withFile = withFile;
		this.withPreservationTitle = withPreservationTitle;
		this.withIdentifer = withIdentifer;
		this.withSourceRegistryId = withSourceRegistryId;
		this.withContractAmount = withContractAmount;
		this.withContractNumber = withContractNumber;
		this.withUserEmail = withUserEmail;
		this.withPhoneNo = withPhoneNo;
		this.withComments = withComments;
	}

	public int getF01() {
		return F01;
	}

	public void setF01(int f01) {
		F01 = f01;
	}

	public String getWithFile() {
		return withFile;
	}

	public void setWithFile(String withFile) {
		this.withFile = withFile;
	}

	public String getWithPreservationTitle() {
		return withPreservationTitle;
	}

	public void setWithPreservationTitle(String withPreservationTitle) {
		this.withPreservationTitle = withPreservationTitle;
	}

	public PersonalIdentifer getWithIdentifer() {
		return withIdentifer;
	}

	public void setWithIdentifer(PersonalIdentifer withIdentifer) {
		this.withIdentifer = withIdentifer;
	}

	public String getWithSourceRegistryId() {
		return withSourceRegistryId;
	}

	public void setWithSourceRegistryId(String withSourceRegistryId) {
		this.withSourceRegistryId = withSourceRegistryId;
	}

	public double getWithContractAmount() {
		return withContractAmount;
	}

	public void setWithContractAmount(double withContractAmount) {
		this.withContractAmount = withContractAmount;
	}

	public String getWithContractNumber() {
		return withContractNumber;
	}

	public void setWithContractNumber(String withContractNumber) {
		this.withContractNumber = withContractNumber;
	}

	public String getWithUserEmail() {
		return withUserEmail;
	}

	public void setWithUserEmail(String withUserEmail) {
		this.withUserEmail = withUserEmail;
	}

	public String getWithPhoneNo() {
		return withPhoneNo;
	}

	public void setWithPhoneNo(String withPhoneNo) {
		this.withPhoneNo = withPhoneNo;
	}

	public String getWithComments() {
		return withComments;
	}

	public void setWithComments(String withComments) {
		this.withComments = withComments;
	}

	@Override
	public String toString() {
		return "EbaoCond [F01=" + F01 + ", withFile=" + withFile
				+ ", withPreservationTitle=" + withPreservationTitle
				+ ", withIdentifer=" + withIdentifer
				+ ", withSourceRegistryId=" + withSourceRegistryId
				+ ", withContractAmount=" + withContractAmount
				+ ", withContractNumber=" + withContractNumber
				+ ", withUserEmail=" + withUserEmail + ", withPhoneNo="
				+ withPhoneNo + ", withComments=" + withComments + "]";
	}
    
}
