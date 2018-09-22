package com.dimeng.p2p.S62.entities
;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S62.enums.T6217_F09;

/**
 * 定向组实体类
 * @author  zhongsai
 * @version  [V7.0, 2018年2月7日]
 */
public class T6217 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
	 * 自增ID
	 */
	public int F01;

	/**
	 * 定向组编号id（如：DX000001）
	 */
	public String F02;

	/**
	 * 定向组名称
	 */
	public String F03;

	/**
	 * 创建人id（对应T7110.F01）
	 */
	public int F04;
	
	/**
	 * 修改人ID（对应T7110.F01）
	 */
	public int F05;

	/**
	 * 创建时间
	 */
	public Timestamp F06;
	
	/**
	 * 最后修改时间
	 */
	public Timestamp F07;
	
	/**
	 * 备注
	 */
	public String F08;
	
    /**
     *  定向组-状态:QY:启用；TY:停用
     */
    public T6217_F09 F09;
	
}
