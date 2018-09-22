package com.dimeng.p2p.modules.finance.console.service.entity;

import com.dimeng.p2p.common.enums.CreditLevel;
import com.dimeng.p2p.common.enums.IsHovestatus;
import com.dimeng.p2p.common.enums.MarriageStatus;
import com.dimeng.p2p.common.enums.Sex;

/**
 * 借款用户详情
 *
 */
public class CreditUserInfo {

	/**
	 * id
	 */
	public int userId;
	/**
	 * 性别(0:女,1:男)
	 */
	public Sex sex;
	/**
	 * 用户名
	 */
	public String userName;
	/**
	 * 年   龄
	 */
	public int age;
	/**
	 * 学   历
	 */
	public String xueli;
	/**
	 * 学   校
	 */
	public String university;
	/**
	 * 婚   姻
	 */
	public MarriageStatus hunyin;
	/**
	 * 公司行业
	 */
	public String companyType;
	/**
	 * 公司规模
	 */
	public String companySize;
	/**
	 * 岗位职责
	 */
	public String gwzz;
	/**
	 * 工作城市
	 */
	public String city;
	/**
	 * 工作时间
	 */
	public String workAge;
	/**
	 * 收入范围
	 */
	public String earnMoey;
	/**
	 * 房产
	 */
	public IsHovestatus isHouse;
	/**
	 * 车产
	 */
	public IsHovestatus isCar;
	/**
	 * 房贷
	 */
	public IsHovestatus isHouseCredit;
	/**
	 * 车贷
	 */
	public IsHovestatus isCarCredit;
	
	/**
	 * 身份证
	 */
	public String card;
	
	/**
	 * 信用等级
	 */
	public CreditLevel level;
	
	
}
