package com.dimeng.p2p.scheduler.tasks;

import java.util.List;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.modules.base.pay.service.DefaultInterestManage;
import com.dimeng.p2p.order.AutoTenderOrderExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;

/**
 * 自动投资
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月28日]
 */
public class AutoBidTask
{
    private static final Logger logger = Logger.getLogger(AutoBidTask.class);
    
    public void autoBid()
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        Boolean isAutoBid = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.IS_AUTOBID));
        if(isAutoBid) {
            logger.info("开始执行【自动投资任务】任务，开始时间：" + new java.util.Date());
            ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
            try (ServiceSession serviceSession = serviceProvider.createServiceSession()) {
                List<Integer> orderIds = serviceSession.getService(DefaultInterestManage.class).autoBid();
                if (orderIds != null && orderIds.size() > 0) {

                    Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
                    AutoTenderOrderExecutor executor = resourceProvider.getResource(AutoTenderOrderExecutor.class);
                    for (int orderId : orderIds) {
                        try {
                            executor.submit(orderId, null);
                            //                            if (!tg) {
                                executor.confirm(orderId, null);
                            //                            }
                        } catch (Exception e) {
                            resourceProvider.log(e);
                        }
                    }
                }
            } catch (Throwable e) {
                resourceProvider.log(e);
            }

            logger.info("结束执行【自动投资任务】任务，结束时间：" + new java.util.Date());
        }
    }
}
