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
import com.dimeng.p2p.escrow.fuyou.executor.FYPrepaymentExecutor;
import com.dimeng.p2p.escrow.fuyou.service.AutoManage;
import com.dimeng.p2p.escrow.fuyou.variables.FuyouVariable;

/**
 * 
 * 提前还款定时自动对账
 * 
 * @author  heshiping
 * @version  [版本号, 2015年12月23日]
 */
public class PrepaymentTask
{
    private static final Logger logger = Logger.getLogger(PrepaymentTask.class);
    
    public void taskPrepayment()
    {
        logger.info("开始执行【提前还款对账】任务，开始时间：" + new java.util.Date());
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            // 查询多久之前的订单
            Long min = Long.valueOf(configureProvider.format(FuyouVariable.FUYOU_AUTORECON_ORDTIME));
            int number = Integer.parseInt(configureProvider.format(FuyouVariable.FUYOU_AUTORECON_ORDTIME));
            Timestamp timestamp = new Timestamp(new Date().getTime() - min * 60 * 1000);
            AutoManage autoManage = serviceSession.getService(AutoManage.class);
            List<T6501> list = autoManage.searchFailedOrder(OrderType.PREPAYMENT_LOAN.orderType(), timestamp, number);
            if (list == null)
            {
                return;
            }
            Map<String, String> params = new HashMap<String, String>();
            String orderIdsStr = "";
            for (int i = 0; i < list.size(); i++)
            {
                if (i == list.size() - 1)
                {
                    orderIdsStr = orderIdsStr + list.get(i).F01;
                }
                else
                {
                    orderIdsStr = orderIdsStr + list.get(i).F01 + ",";
                }
            }
            params.put("orderIdsStr", orderIdsStr);
            FYPrepaymentExecutor executor = resourceProvider.getResource(FYPrepaymentExecutor.class);
            for (T6501 t6501 : list)
            {
                /*
                 * 提前还款对账处理
                 * 避免创建多个数据库连接，将处理放到一个方法中
                 */
                autoManage.antoPrepaymentDZ(executor, serviceSession, t6501, params);
            }
        }
        catch (IOException e)
        {
            logger.error("获取系统常量出错", e);
        }
        catch (Throwable e)
        {
            logger.error("查询出错-提前还款对账", e);
        }
        finally
        {
            logger.info("结束执行【提前还款对账定时任务】任务，结束时间：" + new java.util.Date());
        }
    }
}
