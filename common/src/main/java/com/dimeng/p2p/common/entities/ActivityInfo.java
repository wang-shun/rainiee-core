package com.dimeng.p2p.common.entities;

import java.math.BigDecimal;
import java.sql.Timestamp;

import com.dimeng.framework.service.AbstractEntity;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
import com.dimeng.p2p.S63.enums.T6340_F09;
import com.dimeng.p2p.S63.enums.T6344_F09;

/**
 * 活动管理信息表
 *
 */
public class ActivityInfo extends AbstractEntity implements Comparable<ActivityInfo>
{
	private static final long serialVersionUID = 1L;
	
	/**
	 * 主键id
	 */
    public int activityId;
	
	/**
	 * 活动编码
	 */
    public String activityCode;
	
	/**
	 * 奖励类型
	 */
    public T6340_F03 rewardType;
	
	/**
	 * 活动类型
	 */
    public T6340_F04 activityType;
	
	/**
	 * 活动名称
	 */
    public String activityName;
	
	/**
	 * 活动开始日期
	 */
    public Timestamp startTime;
	
	/**
	 * 活动结束日期
	 */
    public Timestamp endTime;
    
    /**
     * 活动状态：DSJ：待上架；YSJ：预上架；JXZ：进行中；YXJ：已下架；YZF：已作废
     */
    public T6340_F08 activityState;
    
    /**
     * 生日赠送领取条件：login：生日当天登录；invest：生日当天投资；all：不限
     */
    public T6340_F09 birthdayType;
    
    /**
     * 活动规则id：参考T6344.F01
     */
    public int ruleId;
    
    /**
     * 发放数量
     */
    public int sendAmount;
    
    /**
     * 价值：红包单位为元，加息卷单位是%
     */
    public BigDecimal amount;
    
    /**
     * 投资、充值金额
     */
    public BigDecimal money;
    
    /**
     * 已送数量
     */
    public int sendedNumber;
    
    /**
     * 使用有效期
     */
    public int expDay;
    
    /**
     * 使用有效期是否为按月计算,S:是;F:否
     */
    public T6344_F09 isMonth;

    /**
     * 体验金投资有效收益期计算方式(true:按月;false:按天)
     * @return
     */
    public String validMethod;

    /**
     * 体验金投资有效收益期
     * @return
     */
    public int validDate;
    
    /**
     * List排序
     */
    @Override
    public int compareTo(ActivityInfo arg0)
    {
        return arg0.money.compareTo(this.money);
    }
}
