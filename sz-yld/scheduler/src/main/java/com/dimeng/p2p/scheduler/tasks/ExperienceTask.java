package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.modules.base.pay.service.ExperienceManage;
import com.dimeng.p2p.order.ExperTenderRepaymentExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;

/**
 * 体验金定时任务
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月28日]
 */
public class ExperienceTask
{
    protected static final ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
    
    private static final Logger logger = Logger.getLogger(ExperienceTask.class);
    /**
     * 体验金失效
     * <功能详细描述>
     */
    public void invalid(){
        logger.info("开始执行【体验金失效定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            serviceSession.getService(ExperienceManage.class).experienceAmountInvalid();
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
        }
        logger.info("结束执行【体验金失效定时任务】任务，结束时间：" + new java.util.Date());
    }
    
    /**
     * 体验金利息返还
     * <功能详细描述>
     */
    public void returnInvest(){
        logger.info("开始执行【体验金利息返还定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            int[] orderIds = serviceSession.getService(ExperienceManage.class).experienceInterestRtn();
            if (orderIds != null && orderIds.length > 0)
            {
                ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
                Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
                ExperTenderRepaymentExecutor executor = resourceProvider.getResource(ExperTenderRepaymentExecutor.class);
                for (int orderId : orderIds)
                {
                    try
                    {
                        executor.submit(orderId, null);
                        executor.confirm(orderId, null);
                        /* if (!tg)
                         {
                             executor.confirm(orderId, null);
                         }*/
                    }
                    catch (Exception e)
                    {
                        resourceProvider.log(e);
                        serviceSession.getService(ExperienceManage.class).changExperienceOrderToWFH(orderId);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
        }
        logger.info("结束执行【体验金利息返还定时任务】任务，结束时间：" + new java.util.Date());
    }
}
