/*
package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.escrow.huanxun.service.HuanXunBankManage;

*/
/**
 * 环迅-银行列表获取-自动任务
 * 文  件  名：HuanXunBanklistCheckTask.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2017年3月9日
 *//*

public class HuanXunBankQueryTask
{
    
    protected static final ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
    private static final Logger logger = Logger.getLogger(HuanXunBankQueryTask.class);
    
    public void queryBanklist()
    {
        logger.info("开始执行【环迅-银行查询】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            try
            {
                HuanXunBankManage bankManage = serviceSession.getService(HuanXunBankManage.class);
                bankManage.createBankQueryRequest();
            }
            catch (Throwable e)
            {
                logger.error(e);
            }
        }
        logger.info("结束执行【环迅-银行查询】任务，结束时间：" + new java.util.Date());
    }

}
*/
