package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.modules.base.pay.service.ZqzrApplyManage;

/**
 * 债权自动下架
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月28日]
 */
public class OffineCreditorRightsTask
{
    private static final Logger logger = Logger.getLogger(OffineCreditorRightsTask.class);
    
    public void offine()
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        logger.info("开始执行【债权自动下架定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            serviceSession.getService(ZqzrApplyManage.class).zqzrInvalid();
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
        }
        logger.info("结束执行【债权自动下架定时任务】任务，结束时间：" + new java.util.Date());
    }
}
