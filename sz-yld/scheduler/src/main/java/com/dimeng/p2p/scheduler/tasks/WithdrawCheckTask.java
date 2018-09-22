package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.service.CheckManageService;
 
/**
 * 提现对账定时器
 * @author sunqiuyan
 *
 */
public class WithdrawCheckTask  
{
	  private static final Logger logger = Logger.getLogger(WithdrawCheckTask.class);
    
	  public void checkTask(){
	        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
	        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
	        logger.info("开始执行【提现对账】任务，开始时间：" + new java.util.Date());
	        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
	        {
	            serviceSession.getService(CheckManageService.class).checkTask( resourceProvider, serviceSession);
	        }
	        catch (Throwable e)
	        {
	        	logger.error(e);
	            
	        }
	        logger.info("结束执行【提现对账】任务，结束时间：" + new java.util.Date());
	    }
}
