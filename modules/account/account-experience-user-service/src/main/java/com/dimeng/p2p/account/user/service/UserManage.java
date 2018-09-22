package com.dimeng.p2p.account.user.service;

import java.sql.SQLException;

import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.service.Service;
import com.dimeng.p2p.account.user.service.entity.UserLog;

public abstract interface UserManage extends Service {

	

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
	public abstract String getAccountName() throws Throwable;

	

	    /**
     * <dt>
     * <dl>
     * 描述：读取用户ID,登录密码校验.
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
     * 描述：写登录日志.
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
     * @param passwordAuthentication
     * @param accountId
     * @throws Throwable
     */
	public void log(int accountId, String ip)
			throws Throwable;
	    
    /**
     * 获取商户客户号 
     * @return
     * @throws Throwable
     */
	public String getUsrCustId() throws Throwable;
	
    /**
    * 获取用户最后登录时的日志信息
    * @return
    * @throws Throwable
    */
    public UserLog getLastLoginTime(int accountId)
        throws Throwable;
    
    /**
     * 判断后台添加的企业，机构是否第一次登陆
     * @return
     * @throws Throwable
     */
	public boolean isFirstLogin() throws Throwable;
	
    public String selectT6119(int F01)
        throws Throwable;
	
	public String getPhoneNum(int accountId) throws Throwable;
	
	public abstract int getUnReadLetter()
		        throws Throwable;
}
