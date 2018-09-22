package com.dimeng.p2p.modules.account.console.service.entity;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.p2p.S61.enums.T6141_F04;
import com.dimeng.p2p.S61.enums.T6147_F04;
import com.dimeng.p2p.common.enums.Sex;

/**
 * 基本信息
 * @author gongliang
 *
 */
public class BasicInfo {
	/**
	 * 用户Id
	 */
	public int userId;
	
	/**
	 * 昵称
	 */
	public String userName;

	/**
	 * 真实姓名
	 */
	public String realName;
	
	/**
	 * 身份证
	 */
	public String identityCard;
	
	/**
	 * 实名认证是否认证
	 */
	public T6141_F04 isSmrz;
	
	/**
	 * 手机
	 */
	public String msisdn;
	
	/**
	 * 邮箱
	 */
	public String mailbox;
	
	/**
	 * 性别
	 */
	public Sex sex;
	
	/**
	 * 出生日期
	 */
	public Timestamp birthDate;
	
	/**
	 * 毕业学校
	 */
	public String graduateSchool;
	
	/**
	 * 公司行业
	 */
	public String companyBusiness;
	
	/**
	 * 公司规模
	 */
	public String companyScale;

	/**
	 * 职位
	 */
	public String position;
	
	/**
	 * 锁定原因
	 */
	public String lockDesc;
	
	/**
	 * 拉黑原因
	 */
	public String blacklistDesc;
	
	/**
	 * 信用分数
	 */
	public int xyfs;
	
	/**
	 * 信用等级
	 */
	public String qualityRating;
	
	/**
	 * 必要认证
	 */
	public int needAttestation;
	
	/**
	 * 必要认证通过
	 */
	public int byrztg;
	
	/**
	 * 可选认证
	 */
	public int notNeedAttestation;
	
	/**
	 * 可选认证通过
	 */
	public int kxrztg;
	
	/**
	 * 净资产
	 */
	public BigDecimal netAssets = new BigDecimal(0);
	
	/**
	 * 理财资产
	 */
	public BigDecimal lczc = new BigDecimal(0);
	
	/**
	 * 借款负债
	 */
	public BigDecimal borrowingLiability = new BigDecimal(0);
	
	/**
	 * 账户余额
	 */
	public BigDecimal accountBalance = new BigDecimal(0);
	
	/**
	 * 注册时间
	 */
	public Timestamp registrationTime;
	
	/**
	 * 逾期次数
	 */
	public int overdueCount;
	
	/**
	 * 严重逾期次数
	 */
	public int seriousOverdue;
	
	/**
	 * 业务员工号
	 */
	public String employNum;
	
	/**
     * 评估等级
     */
    public T6147_F04 riskAssess;
	
    /**
     * 已评估总次数
     */
    public int assessedNum;
}
