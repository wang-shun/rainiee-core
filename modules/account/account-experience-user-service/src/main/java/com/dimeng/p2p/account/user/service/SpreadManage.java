package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6111_F06;
import com.dimeng.p2p.account.user.service.entity.SpreadEntity;
import com.dimeng.p2p.account.user.service.entity.SpreadTotle;

public abstract interface SpreadManage extends Service {

	/**
	 * 获取我的邀请码
	 * @return
	 */
	public abstract String getMyyqNo() throws Throwable;
	
	/**
	 * 获取我的所有奖励记录
	 * @return
	 * @throws Throwable
	 */
	public abstract PagingResult<SpreadEntity> getAllReward(Paging paging) throws Throwable;
	/**
	 * 查询头部统计
	 * @return
	 * @throws Throwable
	 */
	public abstract SpreadTotle search()throws Throwable;
	
	/**
	 * 得到手机或普通的邀请码 
	 * @return
	 * @throws Throwable
	 */
	public abstract T6111_F06 getT6111_F06()throws Throwable;
	
	/**
	 * 获取邀请码 手机号或普通的邀请码
	 * @return
	 * @throws Throwable
	 */
	public abstract String getMyNewYqm()throws Throwable;
	/**
	 * 补填邀请码
	 * @return
	 * @throws Throwable
	 */
	public abstract void updateYqm(String yqm,int userID)throws Throwable;
	/**
	 * 检查邀请码是否存在
	 * @return
	 * @throws Throwable
	 */
	public int checkExistsYqm(String code, int userId) throws Throwable;
}
