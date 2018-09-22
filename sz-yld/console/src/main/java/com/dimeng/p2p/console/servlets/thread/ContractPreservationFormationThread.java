/*
 * 文 件 名:  ContractPreservationFormationThread.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月23日
 */
package com.dimeng.p2p.console.servlets.thread;

import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.repeater.preservation.ContractPreservationManage;
import com.dimeng.p2p.service.ContractManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/**
 * <一句话功能简述> 放款生成PDF合同文档线程类
 * <功能详细描述>
 * 
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月23日]
 * @since 7.2.0
 */
public class ContractPreservationFormationThread implements Runnable
{
    
    private ResourceProvider resourceProvider;
    
    private ConfigureProvider configureProvider;
    
    /**
     * 标的ID
     */
    private int bidId;
    
    /**
     * 判断线程是否停止
     */
    private boolean stop = false;
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    public ContractPreservationFormationThread(int bidId)
    {
        this.resourceProvider = ResourceProviderUtil.getResourceProvider();
        this.configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        this.bidId = bidId;
    }
    
    @Override
    public void run()
    {
        try (ServiceSession serviceSession = resourceProvider.getResource(ServiceProvider.class).createServiceSession())
        {
            String charset = resourceProvider.getCharset();
            logger.info("charset=" + charset);
            ContractPreservationManage contractPreservationManage =
                serviceSession.getService(ContractPreservationManage.class);
            ContractManage contractManage = serviceSession.getService(ContractManage.class);
            PdfFormationExecutor pdfFormationExecutor = resourceProvider.getResource(PdfFormationExecutor.class);
            
            try
            {
                T6271[] t6271s = contractPreservationManage.getT6271s(bidId);
                
                /*web工程路径*/
                StringBuffer webUrl = new StringBuffer();
                webUrl.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                    .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                    .append("/console/");
                
                Dzxy dzxy = contractManage.getBidContent(bidId);
                if (dzxy == null || (dzxy != null && StringHelper.isEmpty(dzxy.content)))
                {
                    logger.info("获取电子合同模版为空");
                    /*设置停止线程执行*/
                    setStop();
                }
                
                /*合同头部和尾部*/
                StringBuffer sb = new StringBuffer();
                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER))
                    .append(dzxy.content)
                    .append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                
                for (T6271 t6271 : t6271s)
                {
                    if (stop)
                    {
                        break;
                    }
                    if (t6271 == null)
                    {
                        logger.info("获取t6271失败!");
                        continue;
                    }
                    Map<String, Object> valueMap = contractManage.getValueMap(t6271.F03, t6271.F02);
                    t6271.F04 = (String)valueMap.get("xy_no");
                    
                    /*转换PDF合同前生成HTML临时文件*/
                    String path =
                        pdfFormationExecutor.createHTML(valueMap,
                            "contract",
                            dzxy.xymc,
                            sb.toString(),
                            charset,
                            t6271.F04);
                    
                    /*根据生成html的路径来生成PDF文件*/
                    if (!StringHelper.isEmpty(path))
                    {
                        t6271.F09 = pdfFormationExecutor.convertHtml2Pdf(path, webUrl.toString(), charset);
                        contractManage.updateT6271PdfPathNo(t6271);
                        logger.info("生成pdf合同文档成功！");
                    }
                    
                }
            }
            catch (Throwable e)
            {
                logger.error(e);
            }
        }
    }
    
    /**
     * @param 对stop进行赋值 
     */
    public void setStop()
    {
        this.stop = true;
    }
    
}
