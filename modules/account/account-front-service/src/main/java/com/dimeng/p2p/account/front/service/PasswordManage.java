package com.dimeng.p2p.account.front.service;

import com.dimeng.framework.service.Service;

public abstract interface PasswordManage extends Service
{
    /**
     * <dt>
     * <dl>
     * 描述：邮箱是否存在
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
     * @param email
     * @return
     * @throws Throwable
     */
    public abstract int emailExist(String email, String accountType)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：手机是否存在
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
     * @param phone
     * @return
     * @throws Throwable
     */
    public abstract int phoneExist(String phone, String accountType)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：用户名是否存在
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
     * @param phone
     * @return
     * @throws Throwable
     */
    public abstract int accountNameExist(String accountName)
        throws Throwable;
    
    /**
     * <dt>
     * <dl>
     * 描述：重置密码.
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
     * @param password
     *            新密码
     * @param verifyCodeAuthentication
     *            校验码验证信息
     * @throws Throwable
     */
    public abstract void updatePassword(String password, int userId)
        throws Throwable;
    
    /**
     * 查询用户的交易密码
     * @param connection
     * @param userId
     * @return
     * @throws Throwable
     */
    public String getJyPassword(int userId)
        throws Throwable;
    
    /**
     * 检查用户是否设置密保问题
     * <功能详细描述>
     * @param id
     * @return
     * @throws Throwable
     */
    public boolean isSetPwdQues(int id)
        throws Throwable;
    
    public abstract int questionNum(int userId)
        throws Throwable;
    
    /**
     * 根据手机号码更新用户密码
     * 
     * @param password 用户新密码
     * @param phone 用户手机号码
     * @throws Throwable 异常信息
     */
    public abstract void updatePasswordByPhone(String password, String phone)
        throws Throwable;
    
    /**
     * 查询当日交易密码输入错误次数
     * 
     * @return int 
     * @throws Throwable 异常信息
     */
    public abstract int psdInputCount()
        throws Throwable;
    
    /**
     * 增加当日交易密码输入错误次数
     * 
     * @return int 
     * @throws Throwable 异常信息
     */
    public abstract void addInputCount()
        throws Throwable;
}
