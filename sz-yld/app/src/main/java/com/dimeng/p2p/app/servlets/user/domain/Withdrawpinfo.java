package com.dimeng.p2p.app.servlets.user.domain;

import java.io.Serializable;

/**
 * 用户风控信息
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年3月22日]
 */
public class Withdrawpinfo implements Serializable
{
	/**
     * 注释内容
     */
    private static final long serialVersionUID = 1255255809866601827L;

    /** 提现手续费计算方式, ED:按额度(默认方式);BL:按比例 */
	private String way;

	// [按比例]提现手续费比例值
	private String proportion;

	// [按额度]提现手续费1-5万收费
	private String poundage1;

	// [按额度]提现手续费5-20万收费
	private String poundage2;

	// 提现最低金额（元）
	private String min;

	// 提现最高金额（元）
	private String max;

	// 托管手续费
	private String tgPoundage;

	// 提现扣费方式
	private String txkfType;
	
	/**
	 * 温馨提示
	 */
	private String twxts;
	
	// 提现需要审核金额
    private String withdrawLimitFund;

	public String getWay() {
		return way;
	}

	public void setWay(String way) {
		this.way = way;
	}

	public String getProportion() {
		return proportion;
	}

	public void setProportion(String proportion) {
		this.proportion = proportion;
	}

	public String getPoundage1() {
		return poundage1;
	}

	public void setPoundage1(String poundage1) {
		this.poundage1 = poundage1;
	}

	public String getPoundage2() {
		return poundage2;
	}

	public void setPoundage2(String poundage2) {
		this.poundage2 = poundage2;
	}

	public String getMin() {
		return min;
	}

	public void setMin(String min) {
		this.min = min;
	}

	public String getMax() {
		return max;
	}

	public void setMax(String max) {
		this.max = max;
	}

	public String getTgPoundage() {
		return tgPoundage;
	}

	public void setTgPoundage(String tgPoundage) {
		this.tgPoundage = tgPoundage;
	}

	public String getTxkfType() {
		return txkfType;
	}

	public void setTxkfType(String txkfType) {
		this.txkfType = txkfType;
	}

	public String getTwxts() {
		return twxts;
	}

	public void setTwxts(String twxts) {
		this.twxts = twxts;
	}

    /** {@inheritDoc} */
     
    @Override
    public String toString()
    {
        return "Withdrawpinfo [way=" + way + ", proportion=" + proportion + ", poundage1=" + poundage1 + ", poundage2="
            + poundage2 + ", min=" + min + ", max=" + max + ", tgPoundage=" + tgPoundage + ", txkfType=" + txkfType
            + ", twxts=" + twxts + "]";
    }

    public String getWithdrawLimitFund()
    {
        return withdrawLimitFund;
    }

    public void setWithdrawLimitFund(String withdrawLimitFund)
    {
        this.withdrawLimitFund = withdrawLimitFund;
    }
}
