/*
 * 文 件 名:  OperateDataManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  God
 * 修改时间:  2016年3月10日
 */
package com.dimeng.p2p.repeater.policy;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6196;
import com.dimeng.p2p.S61.entities.T6197;
import com.dimeng.p2p.S70.entities.T7054;
import com.dimeng.p2p.repeater.policy.entity.AgeDistributionEntity;
import com.dimeng.p2p.repeater.policy.entity.InvestmentLoanEntity;
import com.dimeng.p2p.repeater.policy.entity.PlatformRiskControlEntity;
import com.dimeng.p2p.repeater.policy.entity.VolumeTimeLimit;
import com.dimeng.p2p.repeater.policy.entity.VolumeType;

/**
 * <运营数据管理> <功能详细描述>
 * 
 * @author zhoucl
 * @version [版本号, 2016年3月10日]
 */
public abstract interface OperateDataManage extends Service {

	/**
	 * 运营数据基础设置
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	public abstract T6196 getT6196() throws Throwable;

	/**
	 * 运营数据累计投资设置
	 * 
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	public abstract List<T6197> getT6197List() throws Throwable;

	/**
	 * 更新运营数据
	 * 
	 * @param t6196
	 *            ,amounts
	 * @return
	 * @throws Throwable
	 */
	public void updateOperateData(T6196 t6196, String[] amounts)
			throws Throwable;

	/**
	 * 获取注册用户数
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract int getRegisterUser() throws Throwable;

	/**
	 * 累计成交笔数
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract int getTradeCount() throws Throwable;

	/**
	 * 投资用户/借款用户分布
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract InvestmentLoanEntity getInvestmentLoanData()
			throws Throwable;

	/**
	 * 平台用户年龄分布
	 * 
	 * @return
	 * @throws Throwable
	 */
	public AgeDistributionEntity[] getAgeRanageData() throws Throwable;

	/**
	 * 借款期限分布
	 * 
	 * @param year
	 * @return
	 * @throws Throwable
	 */
	public abstract VolumeTimeLimit[] getVolumeTimeLimits() throws Throwable;

	/**
	 * 产品类型分布
	 * 
	 * @param year
	 * @return
	 * @throws Throwable
	 */
	public abstract VolumeType[] getVolumeTypes() throws Throwable;

	/**
	 * 平台风险管控实体 <功能详细描述>
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract PlatformRiskControlEntity getPlatformRCE() throws Throwable;

	/**
	 * 平台累计交易数据 <功能详细描述>
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract BigDecimal[] getTotalInvestAmount() throws Throwable;

	/**
	 * <运营数据统计> <功能详细描述>
	 * 
	 * @throws Throwable
	 */
	public void businessStatic() throws Throwable;

	/**
	 * <查询运营数据> <功能详细描述>
	 * 
	 * @return
	 * @throws Throwable
	 */
	public T7054 getT7054() throws Throwable;
	
	/**
	 * <运营数据统计截止时间>
	 * <功能详细描述>
	 * @return
	 */
	public Date getStatisticalDate()throws Throwable;

}
