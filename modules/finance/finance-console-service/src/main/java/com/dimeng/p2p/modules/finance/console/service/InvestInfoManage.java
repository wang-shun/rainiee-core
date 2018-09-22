package com.dimeng.p2p.modules.finance.console.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.common.enums.CreditType;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditFiles;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditInfo;
import com.dimeng.p2p.modules.finance.console.service.entity.CreditUserInfo;
import com.dimeng.p2p.modules.finance.console.service.entity.TenderRecord;
import com.dimeng.p2p.modules.finance.console.service.entity.UserRZInfo;

/**
 * 散标详情接口
 * 
 * @author gaoshaolong
 * 
 */
public interface InvestInfoManage extends Service {

	/**
	 * 获取用户信息
	 * 
	 * @param 借款标id
	 * @return
	 */
	public CreditUserInfo getUser(int id, CreditType creditType, String htfblx)
			throws Throwable;

	/**
	 * 获取用户信用档案
	 * 
	 * @param 借款标id
	 * @return
	 */
	public abstract CreditFiles getFile(int id) throws Throwable;

	/**
	 * 获取投资记录
	 * 
	 * @param 借款标id
	 * @param paging
	 * @return
	 */
	public abstract TenderRecord[] getRecord(int id) throws Throwable;

	/**
	 * 用户认证信息
	 * 
	 * @param 借款标id
	 * @return
	 * @throws Throwable
	 */
	public abstract UserRZInfo[] getRZInfo(int id) throws Throwable;

	/**
	 * 获取散标详细信息.
	 * 
	 * @param id
	 * @return {@link CreditInfo}
	 * @throws Throwable
	 */
	public abstract CreditInfo get(int id) throws Throwable;

}
