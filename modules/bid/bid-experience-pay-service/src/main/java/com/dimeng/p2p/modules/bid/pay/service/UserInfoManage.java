package com.dimeng.p2p.modules.bid.pay.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;

/**
 * 用户信息管理
 */
public interface UserInfoManage extends Service {
	/**
	 * 查询用户是否逾期
	 * @return
	 * @throws Throwable
	 */
	public abstract String isYuqi() throws Throwable;
	/**
	 * 是否实名认证
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isSmrz() throws Throwable;
	/**
	 * 是否设置交易密码
	 * @return
	 * @throws Throwable
	 */
	public abstract boolean isTxmm() throws Throwable;
	
	/**
	 * 查询用户资金信息
	 * @return
	 * @throws Throwable
	 */
	public abstract T6101 search()throws Throwable;

    /**
     * 查询用户风险备用金资金信息
     * @return
     * @throws Throwable
     */
    public abstract T6101 searchFxbyj()throws Throwable;
	/**
	 * 获取登陆用户姓名
	 * @param userID
	 * @return
	 * @throws Throwable
	 */
	public abstract String getUserName(int userID)throws Throwable;
	
	/**
	 * 查询用户基本信息
	 * @param userId
	 * @return
	 * @throws Throwable
	 */
	public abstract T6110 getUserInfo(int userId)throws Throwable;
	
    /** 操作类别
     *  日志内容
     * @param type
     * @param log
     * @throws Throwable
     */
    public void writeFrontLog(String type, String log)
        throws Throwable;

}
