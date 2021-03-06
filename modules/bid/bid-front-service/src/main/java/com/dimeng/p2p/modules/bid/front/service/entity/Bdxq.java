package com.dimeng.p2p.modules.bid.front.service.entity;

import java.sql.Timestamp;

import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.enums.T6216_F18;

public class Bdxq extends T6230{

	 /**
	 * 
	 */
	private static final long serialVersionUID = 9095621057909103029L;
	/** 
     * 等级名称
     */
    public String djmc;
    /** 
     * 进度
     */
    public double proess;
    /**
	 * 筹款结束时间
	 */
	public Timestamp jsTime;
    
    /**
     * 是否有项目动态
     */
    public boolean isXmdt;
   
    /**
     * 产品风险等级
     */
    public T6216_F18  productRiskLevel;
    
    /**
     * 担保方ID
     */
    public int assureId;
}
