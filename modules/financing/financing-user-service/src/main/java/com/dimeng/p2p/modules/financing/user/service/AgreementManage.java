package com.dimeng.p2p.modules.financing.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.modules.financing.user.service.entity.Borrower;
import com.dimeng.p2p.modules.financing.user.service.entity.CreditDetail;
import com.dimeng.p2p.modules.financing.user.service.entity.CreditTrs;
import com.dimeng.p2p.modules.financing.user.service.entity.JoinedUser;
import com.dimeng.p2p.modules.financing.user.service.entity.Organization;
import com.dimeng.p2p.modules.financing.user.service.entity.YxDetail;
import com.dimeng.p2p.modules.financing.user.service.entity.YxJoined;
import com.dimeng.p2p.modules.financing.user.service.entity.YxUser;
import com.dimeng.p2p.modules.financing.user.service.entity.ZqzrCreditDetail;
import com.dimeng.p2p.modules.financing.user.service.entity.ZqzrInfo;
import com.dimeng.p2p.modules.financing.user.service.entity.ZqzrUser;

public abstract interface AgreementManage extends Service {
	/**
	 * 获取加入用户
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract JoinedUser[] getJoinedUser(int creditId) throws Throwable;

	/**
	 * 获取借款者信息
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract Borrower getBorrower(int creditId) throws Throwable;

	/**
	 * 获取标的详情
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract CreditDetail getCreditDetail(int creditId) throws Throwable;

	/**
	 * 获取债权转让信息
	 * 
	 * @param creditId
	 * @return
	 * @throws Throwable
	 */
	public abstract CreditTrs[] getCreditTransfers(int creditId)
			throws Throwable;

	/**
	 * 获取担保机构
	 * 
	 * @param creditId
	 * @return
	 * @throws Throwable
	 */
	public abstract Organization getOrganization(int creditId) throws Throwable;
	
	/**
	 * 获取表的类型
	 * @param creditId
	 * @return
	 * @throws Throwable
	 */
	public abstract CreditType getCreditType(int creditId) throws Throwable;
	/**
	 * 获取加入优选用户信息
	 * @param yxID
	 * @return
	 * @throws Throwable
	 */
	public abstract YxUser getYxUser() throws Throwable;
	/**
	 * 优选加入记录
	 * @param yxID
	 * @return
	 * @throws Throwable
	 */
	public abstract YxJoined[] getYxJoineds(int yxID) throws Throwable;
	/**
	 * 优选期限时间
	 * @param yxID
	 * @return
	 * @throws Throwable
	 */
	public abstract YxDetail getYxDeadline(int yxID) throws Throwable;
	/**
	 * 获取转出者
	 * @param id 债权转让-转入表自增ID
	 * @return
	 * @throws Throwable
	 */
	public abstract ZqzrUser getZcz(int id) throws Throwable;
	/**
	 * 获取转入者
	 * @param id 债权转让-转入表自增ID
	 * @return
	 * @throws Throwable
	 */
	public abstract ZqzrUser getZrz(int id) throws Throwable;
	/**
	 * 获取债权转让的标的详情
	 * @param id 债权转让-转入表自增ID
	 * @return
	 * @throws Throwable
	 */
	public abstract ZqzrCreditDetail getZqzrCreditDetail(int id) throws Throwable;
	/**
	 * 获取债权转让信息
	 * @param id 债权转让-转入表自增ID
	 * @return
	 * @throws Throwable
	 */
	public abstract ZqzrInfo getZqzrInfo(int id) throws Throwable;
}
