package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.order.UnFreezeExecutor;

/**
 *  易宝托管自动解冻自动任务 
 * @author dengwenwu
 *
 */
public class UnFreezeTask {
	 protected static final ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
	    
	    private static final Logger logger = Logger.getLogger(UnFreezeTask.class);

	    /**
	     * 冻结金额自动解冻
	     * <功能详细描述>
	     */
	    public void returnInterest(){
	        logger.info("开始执行【冻结金额自动解冻】任务，开始时间：" + new java.util.Date());
	        
	        UnFreezeExecutor configureProvider = resourceProvider.getResource(UnFreezeExecutor.class);
	        try {
				configureProvider.submit(null);
			} catch (Throwable e) {
				logger.error(e);
			}
	               
	        logger.info("结束执行【冻结金额自动解冻】任务，结束时间：" + new java.util.Date());
	    }
}
