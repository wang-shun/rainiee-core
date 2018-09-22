package com.dimeng.p2p.S61.entities
;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S61.enums.T6119_EXT_F10;

/** 
 * 协议支付签约信息
 */
public class T6119_EXT extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /** 
     * 签约ID,自增
     */
    public int F01;

    /**
	 * 用户ID,参考T6110.F01
	 */
	public int F02;

    /**
	 * 批次号
	 */
    public String F03;

    /** 
     * 协议号
     */
    public String F04;

    /**
	 * 银行卡号/账号
	 */
    public String F05;

	/**
	 * 账号名
	 */
	public String F06;

	/**
	 * 开户证件类型
	 */
	public String F07;

	/**
	 * 证件号
	 */
	public String F08;

	/**
	 * 手机号
	 */
	public String F09;

	/**
	 * 是否签约
	 */
	public T6119_EXT_F10 F10;

	/**
	 * 签约时间
	 */
	public Timestamp F11;
}
