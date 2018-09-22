/*
 * 文 件 名:  UnFreezeTask.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  lingyuanjie
 * 修改时间:  2016年6月3日
 */
package com.dimeng.p2p.escrow.fuyou.task;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.order.UnFreezeExecutor;

/**
 * 富友 - 资金解冻定时任务
 * <功能详细描述>
 * 
 * @author  lingyuanjie
 * @version  [版本号, 2016年6月3日]
 */
public class UnFreezeTask
{
    
    protected static final ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
    
    private static final Logger logger = Logger.getLogger(UnFreezeTask.class);
    
    /**
     * 冻结金额自动解冻
     * <功能详细描述>
     */
    public void unFreezeTask()
    {
        
        logger.info("UnFreezeTask.unFreezeTask()" + new java.util.Date());
        UnFreezeExecutor configureProvider = resourceProvider.getResource(UnFreezeExecutor.class);
        try
        {
            configureProvider.submit(null);
        }
        catch (Throwable e)
        {
            logger.error(e);
        }
    }
    
}
