/*
 * 文 件 名:  QyRegisterCheckTask.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  企业机构注册定时器扫描
 * 修 改 人:  邓文武
 * 修改时间:  2015年12月21日
 */
package com.dimeng.p2p.scheduler.tasks;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.order.QyRegisterCheckExecutor;

/**
 * 企业机构注册定时器扫描
 * @author dengwenwu
 *
 */
public class QyRegisterCheckTask {
    
	protected static final ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
    
    private static final Logger logger = Logger.getLogger(QyRegisterCheckTask.class);

    public void qyCheck(){
        logger.info("开始执行【企业机构注册审核】任务，开始时间：" + new java.util.Date());
        
        QyRegisterCheckExecutor configureProvider = resourceProvider.getResource(QyRegisterCheckExecutor.class);
        try {
			configureProvider.submit(null);
		} catch (Throwable e) {
			logger.error(e);
		}
               
        logger.info("结束执行【企业机构注册审核】任务，结束时间：" + new java.util.Date());
    }
	
}
