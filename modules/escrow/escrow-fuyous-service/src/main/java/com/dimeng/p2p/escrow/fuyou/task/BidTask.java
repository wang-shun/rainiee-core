package com.dimeng.p2p.escrow.fuyou.task;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.escrow.fuyou.service.AutoManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;

/**
 * 
 * 投资自动对账任务
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月19日]
 */
public class BidTask
{
    private static final Logger logger = Logger.getLogger(BidTask.class);
    
    public void taskBid()
    {
        logger.info("开始执行【投资对账】任务，开始时间：" + new java.util.Date());
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            // 查询多久之前的订单
            int number = Integer.parseInt(configureProvider.format(FuyouVariable.FUYOU_AUTORECON_ORDTIME));
            Timestamp timestamp = new Timestamp(new Date().getTime() - 10 * 60 * 1000);
            AutoManage autoManage = serviceSession.getService(AutoManage.class);
            autoManage.searchBidFailedOrder(serviceSession, timestamp, number);
        }
        catch (IOException e)
        {
            logger.error(e);
        }
        catch (Throwable e)
        {
            logger.error(e);
        }
        logger.info("结束执行【投资对账定时任务】任务，结束时间：" + new java.util.Date());
    }
}
