package com.dimeng.p2p.modules.statistics.console.service;

import java.util.Date;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.statistics.console.service.entity.UserOnlineData;

public abstract interface UserOnlineManage extends Service {
	
	/**
	 * 
	 * <dt>
	 * <dl>
	 * 描述：获取指定日期内的在线用户统计数据列表
	 * </dl>
	 * 
	 * <dl>
	 * 数据校验：
	 * <ol>
	 * <li>如果{@code date == null} 则直接退出</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 逻辑校验：
	 * <ol>
	 * <li>无</li>
	 * </ol>
	 * </dl>
	 * 
	 * <dl>
	 * 业务处理：
	 * <ol>
	 * 查询字段列表:
	 * <ol>
	 * </ol>
	 * </li>
	 * </ol>
	 * </dl>
	 * </dt>
	 * 
	 * @param date 日期
	 * @return {@link UserOnlineData}{@code []}查询结果
	 * @throws Throwable
	 */
	public abstract UserOnlineData[] getUserOnlineDatas(Date date) throws Throwable;
}
