package com.dimeng.p2p.modules.bid.user.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.S61.entities.T6161;
import com.dimeng.p2p.S62.entities.T6253;

/**
 * 
 * 电子协议--垫付
 * <功能详细描述>
 * 
 * @author  pengwei
 * @version  [版本号, 2016年6月20日]
 */
public class DzxyDf {
	/**
	 * 协议编号
	 */
	public String xy_no;
	    
	/**
	 * 标的主信息
	 */
	public Bdxq bdxq;
	    
    /**
     * 垫付金额
     */
    public BigDecimal df_amount = new BigDecimal(0);
    
    /**
     * 垫付记录
     */
    public T6253 t6253;
    
    /**
     * 垫付用户信息
     */
    public T6110 t6110;
    
    /**
     * 垫付用户基本信息
     */
    public T6141 t6141;
    
    /**
     * 垫付用户公司信息
     */
    public T6161 t6161;
    
    /**
     * 被垫付人信息
     */
    public T6110 bdft6110;
    
    /**
     * 被垫付人基本信息
     */
    public T6141 bdft6141;
    
    /**
     * 被垫付人公司信息
     */
    public T6161 bdft6161;
}
