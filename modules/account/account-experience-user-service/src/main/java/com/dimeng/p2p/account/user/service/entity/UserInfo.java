package com.dimeng.p2p.account.user.service.entity;


import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F07;
import com.dimeng.p2p.S61.enums.T6110_F08;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S61.enums.T6141_F03;
import com.dimeng.p2p.S61.enums.T6141_F04;

public class UserInfo extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 用户ID,自增
     */
    public int F01;

    /** 
     * 用户登录账号
     */
    public String F02;

    /** 
     * 用户登录密码
     */
    public String F03;

    /** 
     * 手机号码
     */
    public String F04;

    /** 
     * 邮箱
     */
    public String F05;

    /** 
     * 用户类型,ZRR:自然人;FZRR:非自然人
     */
    public T6110_F06 F06;

    /** 
     * 状态,QY启用;SD:锁定;HMD:黑名单;
     */
    public T6110_F07 F07;

    /** 
     * 注册来源,ZC:注册;HTTJ:后台添加
     */
    public T6110_F08 F08;

    /** 
     * 注册时间
     */
    public Timestamp F09;

    /** 
     * 担保方,S:是;F:否;
     */
    public T6110_F10 F10;

    /** 
     * 登录账号ID
     */
    public int F11;

    /** 
     * 姓名
     */
    public String F12;

    /** 
     * 兴趣类型,LC:理财;JK:借款
     */
    public T6141_F03 F13;

    /** 
     * 实名认证,TG:通过;BTG:不通过;
     */
    public T6141_F04 F14;

    /** 
     * 用户头像文件编码
     */
    public String F15;

    /** 
     * 身份证号,3-18位星号替换
     */
    public String F16;

    /** 
     * 身份证号,加密存储,唯一
     */
    public String F17;
    
	/**
	 * 年龄
	 */
	public int age;
	/**
	 * 性别
	 */
	public int sex;
	
	/**
	 * 持有债权数量
	 */
	public int cyzqsl;
	/**
	 * 持有理财计划数量
	 */
	public int cylcjhsl;
	/**
	 * 发布借款数量
	 */
	public int fbjksl;
	/**
	 * 成功借款数量
	 */
	public int cgjksl;
	/**
	 * 未还清借款数量
	 */
	public int whqjksl;
	/**
	 * 逾期次数
	 */
	public int yqcs;
	/**
	 * 逾期金额
	 */
	public BigDecimal yqje=new BigDecimal(0);
	/**
	 * 严重逾期次数
	 */
	public int yzyqcs;
	/**
	 * 信用积分
	 */
	public int xyjf;

}