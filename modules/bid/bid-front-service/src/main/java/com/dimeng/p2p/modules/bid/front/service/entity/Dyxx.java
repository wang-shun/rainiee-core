package com.dimeng.p2p.modules.bid.front.service.entity;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 标抵押信息
 *
 */
public class Dyxx extends AbstractEntity{

	 private static final long serialVersionUID = 1L;

	    /** 
	     * 自增ID
	     */
	    public int F01;

	    /** 
	     * 标ID,参考T6230.F01
	     */
	    public int F02;

	    /** 
	     * 抵押物类型ID,参考T6213.F01
	     */
	    public int F03;

	    /** 
	     * 抵押物名称
	     */
	    public String F04;

	    /** 
	     * 抵押物属性ID,参考T6214.F01
	     */
	    public int F05;

	    /** 
	     * 属性值
	     */
	    public String F06;
	    /** 
	     * 抵押物名称,参考T6213.F02
	     */
	    public String F07;
	    /** 
	     * 抵押物属性名称,参考T6214.F03
	     */
	    public String F08;
	
}
