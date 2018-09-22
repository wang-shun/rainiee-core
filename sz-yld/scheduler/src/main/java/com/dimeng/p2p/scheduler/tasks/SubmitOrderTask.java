package com.dimeng.p2p.scheduler.tasks;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.modules.base.pay.service.OrderManage;
import com.dimeng.p2p.order.AbstractOrderExecutor;
import com.dimeng.p2p.order.FinancialPurchaseExecutor;
import com.dimeng.p2p.order.FinancialRepaymentExecutor;
import com.dimeng.p2p.order.TenderCancelExecutor;
import com.dimeng.p2p.order.TenderConfirmExecutor;
import com.dimeng.p2p.order.TenderExchangeExecutor;
import com.dimeng.p2p.order.TenderOrderExecutor;
import com.dimeng.p2p.order.TransferExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;

/**
 * 定时提交执行订单,5秒中请求一次
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月28日]
 */
public class SubmitOrderTask
{
    private static final Logger logger = Logger.getLogger(SubmitOrderTask.class);
    public void submitOrder()
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        logger.info("开始执行【提交执行订单定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
            
            T6501 order = null;
            if (tg)//托管模式仅查询转账类型订单
                order = serviceSession.getService(OrderManage.class).getToSubmit(OrderType.TRANSFER);
            else
                order = serviceSession.getService(OrderManage.class).getToSubmit();
            if (order != null)
            {
                AbstractOrderExecutor executor = getExecutor(resourceProvider, order.F02);
                if (executor != null)
                {
                    // 提交订单
                    try
                    {
                    	Map<String, String> params = new HashMap<String, String>();
                    	String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
                    	params.put("escrow", escrow);
                        executor.submit(order.F01, params);
                        if (!tg)
                        {
                            executor.confirm(order.F01, null);// 确认订单
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
        logger.info("结束执行【提交执行订单定时任务】任务，结束时间：" + new java.util.Date());
    }
    
    protected AbstractOrderExecutor getExecutor(final ResourceProvider resourceProvider, int orderType)
    {
        switch (orderType)
        {
        //      case 10001: {
        //          return resourceProvider.getResource(ChargeOrderExecutor.class);
        //      }
            case 20001:
            {
                return resourceProvider.getResource(TenderOrderExecutor.class);
            }
            case 20002:
            {
                return resourceProvider.getResource(TenderCancelExecutor.class);
            }
            case 20003:
            {
                return resourceProvider.getResource(TenderConfirmExecutor.class);
            }
            // case 20004: {
            // return resourceProvider.getResource(TenderRepaymentExecutor.class);
            // }
            case 20005:
            {
                return resourceProvider.getResource(TenderExchangeExecutor.class);
            }
            case 30001:
            {
                return resourceProvider.getResource(FinancialPurchaseExecutor.class);
            }
            case 30002:
            {
                return resourceProvider.getResource(FinancialRepaymentExecutor.class);
            }
            case 50001:
            {
                return resourceProvider.getResource(TransferExecutor.class);
            }
            default:
                return null;
        }
    }
}
