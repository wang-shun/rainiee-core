package com.dimeng.p2p.modules.financing.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.modules.financing.user.service.entity.UserInfo;
/**
 * 理财公用登陆用户信息接口
 *
 */
public interface UserInfoManage extends Service {

	/**
	 * 获取登陆用户的信息
	 * @return
	 * @throws Throwable
	 */
	public abstract UserInfo search()throws Throwable ;
}
