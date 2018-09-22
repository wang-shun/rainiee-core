package com.dimeng.p2p.account.front.service;

import java.sql.SQLException;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.Service;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.S61.enums.T6110_F06;

public abstract interface UserManage extends Service
{
    
    /**
     * 
     * <dt>
     * <dl>
     * 描述：获取当前登录用户账户名称.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
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
     * <li>...</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @return
     * @throws Throwable
     */
    public abstract String getAccountName()
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述： 查询用户名是否存在.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
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
     * <li>...</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @param accountName
     * @return
     * @throws Throwable
     */
    public abstract boolean isAccountExists(String accountName)
        throws Throwable;
    
    /**
     * 读取用户ID
     * 
     * @param condition
     * @param password
     * @return
     * @throws AuthenticationException
     * @throws SQLException
     */
    public int readAccountId(String condition, String password)
        throws AuthenticationException, SQLException;
    
    /**
     * <dt>
     * <dl>
     * 描述：获取当前登录用户未读站内信条数.
     * </dl>
     * 
     * <dl>
     * 数据校验：
     * <ol>
     * <li>无</li>
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
     * <li>...</li>
     * </ol>
     * </dl>
     * 
     * <dl>
     * 返回结果说明：
     * <ol>
     * <li>无</li>
     * </ol>
     * </dl>
     * </dt>
     * 
     * @return
     * @throws Throwable
     */
    public abstract int getUnReadLetter()
        throws Throwable;
    
    public void log(int accountId, String ip)
        throws Throwable;
    
    /**
     * 获取商户客户号 
     * @return
     * @throws Throwable
     */
    public String getUsrCustId()
        throws Throwable;
    
    /**
     * 获取用户授权情况（双乾独用）
     * @return
     * @throws Throwable
     */
    public T6119 getUsrAute()
        throws Throwable;
    
    /**
     * 判断后台添加的企业，机构是否第一次登陆
     * @return
     * @throws Throwable
     */
    public boolean isFirstLogin()
        throws Throwable;
    
    /**
     * 后台添加的企业，机构是否第一次登陆修改密码
     * @throws Throwable
     */
    public abstract void updatePassword(String password)
        throws Throwable;
    
    /**
     * 获取用户头像编码
     * @return
     * @throws Throwable
     */
    public abstract String getUserHeadPhoto()
        throws Throwable;
    
    /**
     * 检查邀请码是否存在
     * @param code
     * @return 返回用户id
     * @throws Throwable
     */
    public abstract int checkCodeExist(String code)
        throws Throwable;
    
    public String selectT6280(int F01)
        throws Throwable;
    
    /**
     * 检查业务员工号是否存在
     * @param code
     * @return 返回用户id
     * @throws Throwable
     */
    public abstract int checkNumExist(String code)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：查询用户类型
     * </dl>
     * 
     * @param userId
     *            用户ID
     * @return T6110_F06 用户类型
     * @throws Throwable
     */
    public abstract T6110_F06 getUserType(int userId)
        throws Throwable;
    
    /** 
     * 校验企业名称是否存在
     * @param enterpriseName
     * @return
     * @throws Throwable
     */
    public abstract boolean isEnterpriseNameExists(String enterpriseName)
        throws Throwable;
    
}
