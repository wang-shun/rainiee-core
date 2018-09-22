package com.dimeng.p2p.escrow.fuyou.task;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.OrderType;
import com.dimeng.p2p.S65.entities.T6501;
import com.dimeng.p2p.S65.enums.T6501_F03;
import com.dimeng.p2p.S65.enums.T6501_F11;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.escrow.fuyou.executor.FYTransferExecutor;
import com.dimeng.p2p.escrow.fuyou.service.OrderManage;

/**
 * 
 * 定时转账任务
 * 
 * @author  heshiping
 * @version  [版本号, 2016年1月22日]
 */
public class TransferTask
{
    private static final Logger logger = Logger.getLogger(TransferTask.class);
    
    public void taskTransfer()
        throws Throwable
    {
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        logger.info("开始执行【提交执行订单定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try(ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            OrderManage manage = serviceSession.getService(OrderManage.class);
            // 每次10条
            ArrayList<T6501> list = manage.getToSubmit(OrderType.TRANSFER);
            if (list == null)
            {
                logger.info("结束执行【提交执行订单定时任务】任务，结束时间：" + new java.util.Date());
                return;
            }
            Map<String, String> params = new HashMap<String, String>();
            FYTransferExecutor executor = resourceProvider.getResource(FYTransferExecutor.class);
            for (T6501 t6501 : list)
            {
                boolean flag = false;
                try
                {
                    switch (t6501.F03.name())
                    {
                        case "SB":
                        	flag = manage.selectFuyou(serviceSession, t6501, params);
                            params.put("flag", String.valueOf(flag));
                            // 先判断时间，如果错误订单
                            if (flag)
                            {
                                executor.confirm(t6501.F01, params);
                            }else{
                            	
                            }
                            break;
                        case "DQR":
                            flag = manage.selectFuyou(serviceSession, t6501, params);
                            params.put("flag", String.valueOf(flag));
                            if (flag)
                            {
                                executor.confirm(t6501.F01, params);
                            }
                            else
                            {
                                executor.submit(t6501.F01, null);
                                executor.confirm(t6501.F01, params);
                            }
                            break;
                        case "DTJ":
                            params.put("flag", String.valueOf(flag));
                            executor.submit(t6501.F01, null);
                            executor.confirm(t6501.F01, params);
                            break;
                    }
                }
                catch (Throwable e)
                {
                    logger.error("定时转账订单:" +t6501.F01 +" 流水号:"+ t6501.F10  + "失败，继教下一条");;
                    continue;
                }
            }
            logger.info("结束执行【提交执行订单定时任务】任务，结束时间：" + new java.util.Date());
        }
    }
}
