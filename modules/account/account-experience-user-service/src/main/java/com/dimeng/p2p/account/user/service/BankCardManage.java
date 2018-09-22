package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6114;
import com.dimeng.p2p.S61.entities.T6141;
import com.dimeng.p2p.account.user.service.entity.Bank;
import com.dimeng.p2p.account.user.service.entity.BankCard;
import com.dimeng.p2p.account.user.service.entity.BankCardQuery;
import com.dimeng.p2p.account.user.service.entity.BankDetail;

/**
 * 银行卡
 * @author Administrator
 *
 */
public interface BankCardManage extends Service {
	
	/**
	 * 添加银行卡
	 * @param query
	 * @throws Throwable
	 */
	public abstract int AddBankCar(BankCardQuery query) throws Throwable;
	
	/**
	 * 查询银行
	 * @param
	 * @return
	 * @throws Throwable
	 */
	public abstract Bank[] getBank() throws Throwable;
	
	/**
	 * 查询添加银行卡信息列表
	 * @param acount
	 * @return
	 * @throws Throwable
	 */
	public abstract BankCard[] getBankCars(String status) throws Throwable;
	
	/**
	 * 查询添加银行卡信息
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract BankCard getBankCar(int id) throws Throwable;
	/**
	 * 查询添加银行卡信息
	 * @param cardnumber 银行卡号
	 * @return
	 * @throws Throwable
	 */
	public abstract BankCard getBankCar(String cardnumber) throws Throwable;
	
	/**
	 * 修改状态
	 * @param id 用户ID
	 * @return status 状态
	 * @throws Throwable
	 */
	public abstract void delete(int id,String status) throws Throwable;
	
	/**
	 * 修改银行卡信息
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract void update(int id,BankCardQuery query) throws Throwable;
	/**
	 * 
	 * @return
	 * @throws Throwable
	 */
	public abstract BankCard[] bankCards() throws Throwable;

	/**
	 * 修改已经停用的银行卡信息
	 * @param id
	 * @return
	 * @throws Throwable
	 */
	public abstract void updateTY(int id,BankCardQuery query, int userId) throws Throwable;
	
	/**
	 * 查询银行卡明细
	 * 
	 * @param banId
	 * @return
	 * @throws Throwable
	 */
	public BankDetail getBankDetail(int banId) throws Throwable;
	
	   /**
     * 查询快捷支付银行
     * @param
     * @return
     * @throws Throwable
     */
    public abstract Bank[] getQuickBank()
        throws Throwable;
    
    public T6114 selectT6114()
            throws Throwable;
            
    public T6141 selectT6141()
            throws Throwable;
    
    /**
     * 判断用户是否添加银行卡
     * @return
     * @throws Throwable
     */
    boolean isBindleBanked()
        throws Throwable;
    
    /**
     * <一句话功能简述> 获取银行卡相关信息
     * <功能详细描述>
     * @param accountId
     * @return
     * @throws Throwable
     */
    public BankCard getBankCard(int accountId)
        throws Throwable;
}
