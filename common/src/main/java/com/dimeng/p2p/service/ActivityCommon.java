package com.dimeng.p2p.service;

import java.math.BigDecimal;
import java.sql.Connection;

import com.dimeng.framework.service.Service;

/**
 * 
 * 活动管理
 * <功能详细描述>
 * 
 * @version  [版本号, 2015年10月9日]
 */
public interface ActivityCommon extends Service{
	/**
	 * 送红包和加息券
	 * @param userId
	 * @throws Throwable
	 */
    void sendActivity(Connection connection, int userId, String rewardType, String activityType, BigDecimal amount,
        int tjId)
        throws Throwable;

	/**
	 * 登录送红包和加息券
	 * @param userId
	 * @throws Throwable
	 */
    void sendActivity(int userId, String rewardType, String activityType)
        throws Throwable;

	/**
	 * 投资时送红包和加息券
	 * @param amount
	 * @param userId
	 * @param connection
	 * @throws Throwable
	 */
    void sendRedAndRest(BigDecimal amount, int userId, Connection connection)
        throws Throwable;

	/**
	 * 自动任务：用户生日送红包和加息券
	 * @throws Throwable
	 */
    void sendActivity()
        throws Throwable;
}
