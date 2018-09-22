/*
 * 文 件 名:  EmailVerifyCodeManage.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年11月10日
 */
package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.service.Service;

/**
 * 邮件生成验证码
 * @author  xiaoqi
 * @version  [版本号, 2015年11月10日]
 */
public interface EmailVerifyCodeManage extends Service{

	/**
	 * 获取验证码
	 * <功能详细描述>
	 * @param uid
	 * @param type
	 * @return
	 * @throws Throwable
	 */
	public abstract String getVerifyCode(int uid,String type) throws Throwable;
	
	/**
	 * 验证验证码
	 * <功能详细描述>
	 * @param uid
	 * @param type
	 * @param verifyCode
	 * @throws Throwable
	 */
	public abstract void authenticateVerifyCode(int uid,String type,String verifyCode)throws Throwable; 
	
	/**
	 * 设置校验码为失效
	 * <功能详细描述>
	 * @param uid
	 * @param type
	 * @throws Throwable
	 */
	public abstract void invalidVerifyCode(int uid,String type) throws Throwable;
}
