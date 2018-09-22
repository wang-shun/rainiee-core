package com.dimeng.p2p.account.front.service;

import java.math.BigDecimal;

import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.entities.T6101;
import com.dimeng.p2p.S61.entities.T6110;
import com.dimeng.p2p.S61.entities.T6118;
import com.dimeng.p2p.S61.entities.T6144;
import com.dimeng.p2p.S61.entities.T6147;
import com.dimeng.p2p.account.front.service.entity.UserInfo;
import com.dimeng.p2p.account.front.service.entity.UserRZInfo;

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
     * 是否有标在待审核
     * @return
     * @throws Throwable
     */
    public abstract boolean isBid()
        throws Throwable;
    
    /**
     * 是否设置邮箱
     * @return
     * @throws Throwable
     */
    public abstract boolean isEmail()
        throws Throwable;
    
    /**
     * 查询用户资金信息
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract T6101 search()
        throws Throwable;
    
    /**
     * 用户认证信息
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract UserRZInfo[] getRZInfo(int userId)
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
     * 黑名单列表
     * @param paging
     * @return
     * @throws Throwable
     */
    public abstract PagingResult<UserInfo> search(Paging paging)
        throws Throwable;
    
    /**
     * 根据身份证获取年龄和性别(1:男，0:女)
     * @param card
     * @return
     * @throws Throwable
     */
    public abstract UserInfo getAgeSex(String card)
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
     * 获取用户的信用积分
     * @return
     * @throws Throwable
     */
    public abstract int getXyjf(int userId)
        throws Throwable;
    
    /**
    * 获取用户的信用等级
    * @return
    * @throws Throwable
    */
    public abstract String getXyLevel(int userId)
        throws Throwable;
    
    /**
     * 根据用户id得到用户信用记录
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract T6144 getXyjl(int userId)
        throws Throwable;
    
    /**
     * 根据用户id得到用户的待还金额
     * @param userId
     * @return
     * @throws Throwable
     */
    public abstract BigDecimal getDhje(int userId)
        throws Throwable;
    
    /**
     * 获取用户安全认证信息
     * @return
     * @throws Throwable
     */
    public boolean getYhrzxx()
        throws Throwable;
    
    public T6118 getT6118(int id)
        throws Throwable;
    
    /**
     * 查询用户风险等级分数
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public T6147 getT6147()
        throws Throwable;
    
    /**
     * 是否已经网签
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public boolean isNetSigned()
        throws Throwable;
}
