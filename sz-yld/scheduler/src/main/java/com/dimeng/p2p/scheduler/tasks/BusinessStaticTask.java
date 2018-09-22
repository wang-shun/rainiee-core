package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.repeater.policy.OperateDataManage;

/**
 * 
 * <运营数据统计>
 * <功能详细描述>
 * 
 * @author  liulixia
 * @version  [版本号, 2018年2月7日]
 */
public class BusinessStaticTask
{
    private static final Logger logger = Logger.getLogger(BusinessStaticTask.class);
    
    public void businessStatic()
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        logger.info("开始执行【运营数据统计定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            serviceSession.getService(OperateDataManage.class).businessStatic();
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
        }
        logger.info("结束执行【运营数据统计定时任务】任务，结束时间：" + new java.util.Date());
    }
}
