package com.dimeng.p2p.modules.account.pay.service;

import java.sql.SQLException;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S61.entities.T6119;
import com.dimeng.p2p.modules.account.pay.service.entity.OpenEscrowGuideEntity;
import com.dimeng.p2p.modules.account.pay.service.entity.QyUserInsert;
import com.dimeng.p2p.modules.account.pay.service.entity.UserInsert;

public abstract interface UserManage extends Service
{
    
    /**
    * <dt>
    * <dl>
    * 描述：用户注册.
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
    * @param user
    * @return
    * @throws Throwable
    */
    public abstract int addUser(UserInsert user, ServiceSession serviceSession)
        throws Throwable;
    
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
    * 记录注册信息
    * @param userId 用户ID
    * @param ip 注册者IP
    * @throws Throwable
    */
    public void logRegisterInfo(int userId, String ip)
        throws Throwable;
    
    /**
    * 根据IP查询注册信息,true:允许注册，false:不允许注册
    * @param ip 注册者IP
    * @throws Throwable
    */
    public boolean ifAllowRegister(String ip)
        throws Throwable;
    
    /**
    * 修改手机号
    * @param phoneNumber 用户注册手机号码
    * @param userId 用户ID
    * @throws Throwable
    */
    public abstract void updatePhoneNum(String phoneNumber, int userId)
        throws Throwable;
    
    /**
     * 更新累计登陆次数和最后登陆时间
     * @return
     * @throws Throwable
     */
    public abstract void udpateT6198F05F07(int userId)
        throws Throwable;
    
    /**
     * 当前用户的查询第三方标识
     * <功能详细描述>
     * @return
     * @throws Throwable
     */
    public abstract String getUsrCustId()
        throws Throwable;
    
    /** 
     * 企业注册
     * @param user
     * @param serviceSession
     * @return
     * @throws Throwable
     */
    public int addQy(QyUserInsert user, ServiceSession serviceSession)
        throws Throwable;

    
    /**
     * 查询用户第三方托管信息
     * 
     * @param F01
     * @return
     * @throws Throwable
     */
    public T6119 selectT6119(int F01)
        throws Throwable;
    
    /**
     * 开户相关信息
     * @return
     * @throws Throwable
     */
    public OpenEscrowGuideEntity getOpenEscrowGuideEntity()
        throws Throwable;
}