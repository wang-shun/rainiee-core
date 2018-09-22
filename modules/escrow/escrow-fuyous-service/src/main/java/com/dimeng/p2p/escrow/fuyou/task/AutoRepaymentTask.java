package com.dimeng.p2p.escrow.fuyou.task;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.escrow.fuyou.service.AutoRepaymentManage;
import com.dimeng.p2p.order.AutoTenderRepaymentExecutor;

/**
 * 
 * 自动还款
 * <功能详细描述>
 * 
 * @author  heshiping
 * @version  [版本号, 2016年3月16日]
 */
public class AutoRepaymentTask
{
    private static final Logger logger = Logger.getLogger(AutoRepaymentTask.class);
    
    public void repayment()
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        logger.info("开始执行【自动还款定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            int[] orderIds = serviceSession.getService(AutoRepaymentManage.class).repayment(serviceSession);
            if (orderIds != null)
            {
                logger.info("自动还款-需还款订单：" + orderIds.length);
                AutoTenderRepaymentExecutor executor = resourceProvider.getResource(AutoTenderRepaymentExecutor.class);
                Map<String, String> params = new HashMap<String, String>();
                int i = 0;
                
                for (int orderId : orderIds)
                {
                    logger.info("第" + ++i + "条自动还款");
                    try
                    {
                        executor.submit(orderId, params);
                        executor.confirm(orderId, params);
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
}
