package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.modules.base.pay.service.DefaultInterestManage;

/**
 * 自动计算罚息
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月27日]
 */
public class CalculationPenaltyTask
{
   
	private static final Logger logger = Logger.getLogger(AutoBidTask.class);
	
    public void calculate(){
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        logger.info("开始执行【自动计算罚息任务】任务，开始时间：" + new java.util.Date());
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            serviceSession.getService(DefaultInterestManage.class).calculate();
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
            
        }
        logger.info("结束执行【自动计算罚息任务】任务，结束时间：" + new java.util.Date());
    }
}
