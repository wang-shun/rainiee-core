package com.dimeng.p2p.S62.entities
;

import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;

/**
 * 定向组>定向用户 实体类
 * @author  zhongsai
 * @version  [V7.0, 2018年2月7日]
 */
public class T6218 extends AbstractEntity{

    private static final long serialVersionUID = 1L;

    /**
	 * 自增ID
	 */
	public int F01;

	    /**
     * 用户ID（对应T6110.F01）
     */
    public int F02;

	    /**
     * 定向组ID（对应T6217.F01）
     */
    public int F03;

	    /**
     * 加入时间
     */
    public Timestamp F04;
	
	
}
