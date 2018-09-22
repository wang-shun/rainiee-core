package com.dimeng.p2p.modules.bid.console.service;

import java.util.List;
import java.util.Map;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S62.entities.T6230;

public interface AddBidManage extends Service {

	/**
	 * 获取待发布标信息记录
	 * 
	 * @return
	 * @throws Throwable
	 */
	public List<T6230> selectT6230s() throws Throwable;

	/**
	 * 汇付录入标的信息
	 * 
	 * @param t6230
	 * @return
	 * @throws Throwable
	 */
	public Map<String, String> addBidInfo(int loanId) throws Throwable;

	/**
	 * 查询汇付标信息的状态
	 * 
	 * @param t6230
	 * @return
	 * @throws Throwable
	 */
	public Map<String, String> checkBidInfo(int loanId) throws Throwable;

	/**
	 * 汇付补录标信息
	 * 
	 * @param proId
	 * @return
	 * @throws Throwable
	 */
	public String bidEnterAgain(String proId) throws Throwable;

}
