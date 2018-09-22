package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6251;
import com.dimeng.p2p.S62.entities.T6260;
import com.dimeng.p2p.S62.entities.T6262;

/**
 * 电子协议-线上债权转让
 * @author gaoshaolong
 *
 */
public class DzxyZqzr {
	/**
	 * 协议编号
	 */
	public String xy_no;
	/**
	 * 用户名（乙方）
	 */
	public String zqzr_yf_loginName;
    
    /**
     * 用户类型（乙方）
     */
    public String zqzr_yf_userType;

	/**
	 * 真实姓名(乙方)
	 */
	public String zqzr_yf_realName;
    
    /**
     * 公司名称(乙方)
     */
    public String zqzr_yf_companyName;
    
    /**
     * 营业执照号/社会信用代码(乙方)
     */
    public String zqzr_yf_papersNum;

	/**
	 * 身份证号(乙方)
	 */
	public String zqzr_yf_sfzh;
	
	/**
	 * 标的主信息
	 */
	public Bdxq bdxq;
	/**
	 * 标的扩展信息
	 */
	public T6231 t6231;
	/**
	 * 债权用户信息
	 */
	public T6110 t6110;
	/**
	 * 债权用户基本信息
	 */
	public T6141 t6141;
    
    /**
     * 债权用户公司信息
     */
    public T6161 t6161;

	/**
	 * 线上债权转让申请
	 */
	public T6260 t6260;
	/**
	 * 线上债权转让记录
	 */
	public T6262 t6262;
	/**
	 * 标债权记录
	 */
	public T6251 t6251;
	/**
	 * 月还本息数额
	 */
	public BigDecimal zqzr_bid_ychbxse = new BigDecimal(0);
	/**
	 * 债权人待收本息
	 */
	public BigDecimal zqzr_zqr_dsbx = new BigDecimal(0);
}
