package com.dimeng.p2p.account.user.service;

import com.dimeng.framework.service.Service;
import com.dimeng.p2p.account.user.service.entity.ThirdUser;

public interface UserThirdPartyRegisterManage extends Service  {

	/**
	 * 
	  * 〈一句话功能简述〉
	  * 插入T6171用户扩展表
	  * @author 王义
	  * @see    UserThirdPartyRegisterManage.java
	  * @since  (注册模块，版本)
	  * @return void
	  * @data 2014年11月28日
	 */
	public void addT6171(ThirdUser userThird)throws Throwable;
}
