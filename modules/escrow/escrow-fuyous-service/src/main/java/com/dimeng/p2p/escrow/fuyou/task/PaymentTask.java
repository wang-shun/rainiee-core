package com.dimeng.p2p.escrow.fuyou.task;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.escrow.fuyou.executor.FYRepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.service.AutoManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;

/**
 * 
 * 手动还款自动对账
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月20日]
 */
public class PaymentTask
{
    
    private static final Logger logger = Logger.getLogger(PaymentTask.class);
    
    public void taskPayment()
    {
        logger.info("开始执行【手动还款对账】任务，开始时间：" + new java.util.Date());
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            // 查询多久之前的订单
            long min = Long.valueOf(configureProvider.format(FuyouVariable.FUYOU_AUTORECON_ORDTIME));
            int number = Integer.parseInt(configureProvider.format(FuyouVariable.FUYOU_AUTORECON_ORDTIME));
            Timestamp timestamp = new Timestamp(new Date().getTime() - min * 60 * 1000);
            AutoManage autoManage = serviceSession.getService(AutoManage.class);
            List<T6501> list = autoManage.searchFailedOrder(OrderType.BID_REPAYMENT.orderType(), timestamp, number);
            if (list == null)
            {
                return;
            }
            FYRepaymentExecutor executor = resourceProvider.getResource(FYRepaymentExecutor.class);
            Map<String, String> params = new HashMap<String, String>();
            for (T6501 t6501 : list)
            {
                /*
                 * 手动还款对账处理
                 * 避免创建多个数据库连接，将处理放到一个方法中
                 */
                autoManage.autoPaymentDZ(executor, serviceSession, t6501, params);
            }
            
        }
        catch (IOException e)
        {
            logger.error(e);
        }
        catch (Throwable e)
        {
            logger.error(e.getMessage());
        }
        logger.info("结束执行【手动还款对账定时任务】任务，结束时间：" + new java.util.Date());
    }
}
