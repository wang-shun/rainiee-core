package com.dimeng.p2p.scheduler.tasks;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.S62.enums.T6273_F10;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;
import com.dimeng.p2p.signature.fdd.variables.FddVariable;

/**
 * 法大大自动签章
 * 文  件  名：FDDExtSignTask.java
 * 版        权：深圳市迪蒙网络科技有限公司
 * 类  描  述：
 * 修  改  人：ZhangXu
 * 修改时间：2016年12月21日
 */
public class FDDExtSignTask
{
    private static final Logger logger = Logger.getLogger(FDDExtSignTask.class);
    
    public void taskExtSign()
    {
        
        logger.info("开始执行【法大大自动签章】任务，开始时间：" + new java.util.Date());
        
        ResourceProvider resourceProvider = ResourceProviderUtil.getResourceProvider();
        ServiceProvider serviceProvider = resourceProvider.getResource(ServiceProvider.class);
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        try (ServiceSession serviceSession = serviceProvider.createServiceSession())
        {
            //查询多久之前的未签名的
            long min = Long.valueOf(configureProvider.format(FddVariable.FDD_AUTORECON_ORDTIME));
            Timestamp timestamp = new Timestamp(new Date().getTime() - min * 60 * 1000);
            //查询法大大待签的合同文档
            FddSignatureServiceV25 autoSignManager = serviceSession.getService(FddSignatureServiceV25.class);
            List<T6273> list = autoSignManager.selectUnExtSign(timestamp);
            if (list == null)
            {
                return;
            }
            
            int orderId = 0;
            for (T6273 t6273 : list)
            {
                try
                {
                    synchronized (FDDExtSignTask.class)
                    {
                        //投资人取投资成功id，债权转让取债权转让id
                        if (t6273.F10.name().equals(T6273_F10.TZR.name()))
                        {
                            orderId = t6273.F14;
                        }
                        else
                        {
                            orderId = t6273.F18;
                        }
                        //调用法大大合同文档上传、签约、归档接口
                        autoSignManager.uploadFile(t6273.F02, t6273.F03, orderId, t6273.F10);
                    }
                }
                catch (Throwable es)
                {
                    logger.error("", es);
                }
            }
        }
        catch (IOException e)
        {
            logger.error(e);
        }
        catch (Throwable e)
        {
            logger.error(e);
        }
        logger.info("结束执行【法大大自动签章】任务，结束时间：" + new java.util.Date());
    }
    
}
