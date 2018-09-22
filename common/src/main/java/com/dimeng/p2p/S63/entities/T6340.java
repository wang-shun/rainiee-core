package com.dimeng.p2p.S63.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
import com.dimeng.p2p.S63.enums.T6340_F09;

/**
 * 活动管理信息表
 * @author heluzhu
 *
 */
public class T6340 extends AbstractEntity{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
	public int F01;
	
	/**
	 * 活动编码
	 */
	public String F02;
	
	/**
	 * 奖励类型
	 */
	public T6340_F03 F03;
	
	/**
	 * 活动类型
	 */
	public T6340_F04 F04;
	
	/**
	 * 活动名称
	 */
	public String F05;
	
	/**
	 * 活动开始日期
	 */
	public Timestamp F06;
	
	/**
	 * 活动结束日期
	 */
	public Timestamp F07;
	
	/**
	 * 活动状态：DSJ：待上架；YSJ：预上架；JXZ：进行中；YXJ：已下架；YZF：已作废
	 */
    public T6340_F08 F08;
	
	/**
	 * 生日赠送领取条件：login：生日当天登录；invest：生日当天投资；all：不限
	 */
    public T6340_F09 F09;
	
	/**
	 * 备注
	 */
    public String F10;
	
	/**
	 * 创建时间
	 */
    public Timestamp F11;
	
	/**
	 * 修改时间
	 */
    public Timestamp F12;
	
	/**
     * 下架、作废原因(活动结束时间;达到活动限制数量;同类型活动复盖;下架操作;作废操作;活动结束时间或达到活动限制数量)
     */
    public String F13;

	/**
	 * 已领取数量(仅用于查询)
	 */
	public int F30;
	
	/**
     * 已领取总额(仅用于查询)
     */
    public BigDecimal F31 = BigDecimal.ZERO;
	
}
