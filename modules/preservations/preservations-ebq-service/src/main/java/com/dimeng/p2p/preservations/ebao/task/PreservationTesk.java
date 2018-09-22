package com.dimeng.p2p.preservations.ebao.task;

import org.apache.log4j.Logger;

import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.preservations.ebao.service.PreservationManager;


/**
 * 
 * 保存保全
 * <易保全-定时任务请求保存>
 * 
 * @author  邓文武
 * @version  [版本号, 2016年6月22日]
 */
public class PreservationTesk
{
	protected static final ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
    private static final Logger logger = Logger.getLogger(PreservationTesk.class);
    
    /**
     * 合同保全
     * @throws Throwable
     */
    public void contractPreservation()
        throws Throwable
    {
    	logger.info("开始执行【合同保全定时任务】任务，开始时间：" + new java.util.Date());
    	ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
        	PreservationManager preservationManager = serviceSession.getService(PreservationManager.class);
        	preservationManager.contractPreservation(0);
        }
        logger.info("结束执行【合同保全定时任务】任务，结束时间：" + new java.util.Date());
    }
    
    
    /**
     * 协议保全
     * @throws Throwable
     */
    public void agreementPreservation()
            throws Throwable
    {
    	logger.info("开始执行【协议保全定时任务】任务，开始时间：" + new java.util.Date());
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
        	PreservationManager preservationManager = serviceSession.getService(PreservationManager.class);
            preservationManager.agreementPreservation(0);
        }
        logger.info("结束执行【协议保全定时任务】任务，结束时间：" + new java.util.Date());
    }
}
