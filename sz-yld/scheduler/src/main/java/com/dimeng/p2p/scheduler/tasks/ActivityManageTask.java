package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.modules.base.pay.service.ActivityManage;
import com.dimeng.p2p.order.ActivityRepaymentExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;

/**
 * 活动管理定时任务
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月28日]
 */
public class ActivityManageTask
{
    protected static final ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
    
    private static final Logger logger = Logger.getLogger(ActivityManageTask.class);

    /**
     * 加息券利息返还
     * <功能详细描述>
     */
    public void returnInterest(){
        logger.info("开始执行【加息券利息返还定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            int[] orderIds = serviceSession.getService(ActivityManage.class).activityInterestRtn();
            if (orderIds != null && orderIds.length > 0)
            {
                ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
                Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
                ActivityRepaymentExecutor executor = resourceProvider.getResource(ActivityRepaymentExecutor.class);
                for (int orderId : orderIds)
                {
                    try
                    {
                        executor.submit(orderId, null);
                        executor.confirm(orderId, null);
                        /*if (!tg)
                        {
                            executor.confirm(orderId, null);
                        }*/
                    }
                    catch (Exception e)
                    {
                        resourceProvider.log(e);
//                        serviceSession.getService(ActivityManage.class).changExperienceOrderToWFH(orderId);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
        }
        logger.info("结束执行【加息券利息返还定时任务】任务，结束时间：" + new java.util.Date());
    }
}
