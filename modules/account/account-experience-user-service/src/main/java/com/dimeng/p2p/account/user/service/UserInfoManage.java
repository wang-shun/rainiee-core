package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.account.user.service.entity.UserInfo;

/**
 * 用户信息管理
 */
public interface UserInfoManage extends Service
{
    /**
     * 查询用户是否逾期
     * @return
     * @throws Throwable
     */
    public abstract String isYuqi()
        throws Throwable;
    
    /**
     * 是否实名认证
     * @return
     * @throws Throwable
     */
    public abstract boolean isSmrz()
        throws Throwable;
    
    /**
     * 是否设置交易密码
     * @return
     * @throws Throwable
     */
    public abstract boolean isTxmm()
        throws Throwable;
    
    /**
     * 查询用户资金信息
     * @return
     * @throws Throwable
     */
    public abstract T6101 search()
        throws Throwable;
    
    /**
     * 查询用户风险备用金资金信息
     * @return
     * @throws Throwable
     */
    public abstract T6101 searchFxbyj()
        throws Throwable;
    
    /**
     * 获取登陆用户姓名
     * @param userID
     * @return
     * @throws Throwable
     */
    public abstract String getUserName(int userID)
        throws Throwable;
    
    /**
     * 查询用户信息
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract UserInfo search(int userId)
        throws Throwable;
    
    /**
     * 查询用户基本信息
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract T6110 getUserInfo(int userId)
        throws Throwable;
    
    /**
     * 根据用户账号名称查询用户基本信息
     * @param accountName
     * @return
     * @throws Throwable
     */
    public abstract T6110 getUserInfoByAccountName(String accountName)
        throws Throwable;
    
    /**
     * 查询用户错误登录次数
     * @param user
     * @param ip
     * @return 用户错误登录次数
     * @throws Throwable
     */
    public abstract int getUserLoginError(UserInfo user, String ip)
        throws Throwable;
    
    /**
     * 清除登录错误次数
     * @param userName
     * @param ip
     */
    public abstract void clearErrorCount(String userName, String ip)
        throws Throwable;
    
    /**
     * 查询用户信息
     * @param userName
     * @return
     * @throws Throwable
     */
    public abstract UserInfo search(String userName)
        throws Throwable;
    
    /**
     * 更新累计登陆次数和最后登陆时间
     * @return
     * @throws Throwable
     */
    public abstract void udpateT6198F05F07(int userId)
        throws Throwable;
    
    /**
     * 获取用户安全认证信息
     * @return
     * @throws Throwable
     */
    public boolean getYhrzxx()
        throws Throwable;
    
}
