package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.modules.base.pay.service.RepaymentManage;
import com.dimeng.p2p.order.AutoTenderRepaymentExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;

/**
 * 自动还款
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月28日]
 */
public class RepaymentTask
{
    private static final Logger logger = Logger.getLogger(RepaymentTask.class);
    
    public void repayment()
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        logger.info("开始执行【自动还款定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            int[] orderIds = serviceSession.getService(RepaymentManage.class).repayment();
            if (orderIds != null)
            {
                logger.info("需还款订单：" + orderIds.length);
                ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
                Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
                AutoTenderRepaymentExecutor executor = resourceProvider.getResource(AutoTenderRepaymentExecutor.class);
                for (int orderId : orderIds)
                {
                    try
                    {
                        executor.submit(orderId, null);
                        if (!tg)
                        {
                            executor.confirm(orderId, null);
                        }
                    }
                    catch (Exception e)
                    {
                        resourceProvider.log(e);
                    }
                }
            }
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
            
        }
        
        logger.info("结束执行【自动还款定时任务】任务，结束时间：" + new java.util.Date());
    }
    
    public void repaymentAll()
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        logger.info("开始执行【自动还款定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            RepaymentManage manage = serviceSession.getService(RepaymentManage.class);
            int[] orderIds = manage.repayment();
            if (orderIds != null && orderIds.length > 0)
            {
                AutoTenderRepaymentExecutor executor = resourceProvider.getResource(AutoTenderRepaymentExecutor.class);
                executor.submitAll(orderIds, null);
                executor.confirmAll(orderIds, null);
            }
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
            
        }
        
        logger.info("结束执行【自动还款定时任务】任务，结束时间：" + new java.util.Date());
    }
    
    /**
     * 易宝自动还款定时任务
     */
    public void yeepayRepayment()
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        logger.info("开始执行【易宝自动还款定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
        	AutoTenderRepaymentExecutor executor = resourceProvider.getResource(AutoTenderRepaymentExecutor.class);
        	executor.yeepaySubmit();
        }
        catch (Throwable e)
        {
            resourceProvider.log(e);
            
        }
        
        logger.info("结束执行【易宝自动还款定时任务】任务，结束时间：" + new java.util.Date());
    }
}
