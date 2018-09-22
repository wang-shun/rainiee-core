/**
 * 
 */
package com.dimeng.p2p.modules.bid.console.service.entity;

import java.math.BigDecimal;

import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.S62.entities.T6250;

/**
 * @author guopeng
 * 
 */
public class TbRecord extends T6250 {

	private static final long serialVersionUID = 1L;

	/**
	 * 投资人用户名
	 */
	public String userName;
	/**
	 * 姓名
	 */
	public String name;
	/**
	 * 身份证
	 */
	public String idCard;
	/**
	 * 联系方式
	 */
	public String phone;


	/**
	 * 购买价格
	 */
	public BigDecimal exAmount = BigDecimal.ZERO;

	/**
	 * 红包
	 */
	public BigDecimal hbAmount = BigDecimal.ZERO;

	/**
	 * 投资方式
	 */
	public String bidWay;
	
	/**
     * 加息利率
     */
    public BigDecimal jxll = BigDecimal.ZERO;

	/**
	 * 用户类型
	 */
	public T6110_F06 userType;

	/**
	 * 是否担保方
	 */
	public T6110_F10 sfdb;
}
