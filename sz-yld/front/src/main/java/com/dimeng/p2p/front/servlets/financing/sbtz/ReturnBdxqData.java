package com.dimeng.p2p.front.servlets.financing.sbtz;

import java.io.Serializable;

import com.dimeng.p2p.S61.entities.T6163;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.S62.entities.T6231;
import com.dimeng.p2p.S62.entities.T6232;
import com.dimeng.p2p.S62.entities.T6233;
import com.dimeng.p2p.S62.entities.T6237;
import com.dimeng.p2p.account.front.service.entity.Enterprise;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdxq;
import com.dimeng.p2p.modules.bid.front.service.entity.Bdylx;
import com.dimeng.p2p.modules.bid.front.service.entity.Dbxx;

public class ReturnBdxqData implements Serializable {
	
	private static final long serialVersionUID = 1L;
	/**
	 * 标的信息
	 */
	public Bdxq bdxq;
	
	/**
	 * 融资方
	 */
	public String qyName;
	
	/**
	 * 项目区域
	 */
	public String projectArea;
	
	/**
	 * 标的扩展信息
	 */
	public T6231 t6231;
	
	/**
	 * 企业信息
	 */
	public Enterprise enterprise;
	
	/**
	 * 企业财务状况
	 */
	public T6163[] t6163s;
	/**
	 * 标的风控信息
	 */
	public T6237 t6237;
	/**
	 * 标的担保信息
	 */
	public Dbxx dbxx;
	/**
	 * 标的抵押物信息
	 */
	public Bdylx dyxxs;
	/**
	 * 附件是否公开
	 */
	public boolean isResult=false;
	/**
	 * 公开附件列表
	 */
	public T6232[] t6232s;
	/**
	 * 非公开附件列表
	 */
	public T6233[] t6233s = null;
	
	/**
	 * 附件类型
	 */
	public T6212[] t6212s = null;

	/**
	 * 是否存在备用金垫付文本
	 */
	public String existByjdf;
    
    /**
     * 信用等级
     */
    public String creditLevel;
}
