package com.dimeng.p2p.account.user.service.entity;

import java.sql.Timestamp;

/**
 * 用户信息
 * @author Administrator
 *
 */
public class UserBase {
	
	/**
	 * 昵称
	 */
	public String userName;
	
	/**
	 * 真实姓名
	 */
	public String name;
	
	/**
	 * 身份证号
	 */
	public String idCard;
	
	/**
	 * 加密的身份证号
	 */
	public String sfzh;
	
	/**
	 * 电话号码
	 */
	public String phoneNumber;
	
	/**
	 * 邮箱
	 */
	public String emil;
	
	/**
	 * 最高学历
	 */
	public String education;
	
	/**
	 * 毕业院校
	 */
	public String school;
	
	/**
	 * 结婚状况
	 */
	public String marriage;
	
	/**
	 * 居住地址
	 */
	public String address;
	
	/**
	 * 公司行业
	 */
	public String companyhy;
	
	/**
	 * 公司规模
	 */
	public String compangm;
	
	/**
	 * 职位名称
	 */
	public String jobtitle;
	
	/**
	 * 收入
	 */
	public String income;
	/**
	 * 图像
	 */
	public String icon;
	
	/**
	 * 出生日期
	 */
	public Timestamp date;

	/**
	 * 个人图片地址
	 */
	public String userImg;

	/**
	 * 性别
	 */
	public String sex;

	/**
	 * 生日
	 */
	public String birthday;
}
