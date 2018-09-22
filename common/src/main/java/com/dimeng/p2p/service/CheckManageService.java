package com.dimeng.p2p.service;
/*
 * 文 件 名:  CheckManageService.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  代付提现任务处理
 * 修 改 人:  huguangze
 * 修改时间:  2014年11月24日
 */

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.Service;
import com.dimeng.framework.service.ServiceSession;

/**
 * 代付提现任务处理
 * 
 * @author  huguangze
 * @version  [版本号, 2014年11月24日]
 */
public interface CheckManageService extends Service
{
	   /**
	    * 提现对账接口
	    */
	     void checkTask(ResourceProvider resourceProvider,ServiceSession serviceSession);
   
}
