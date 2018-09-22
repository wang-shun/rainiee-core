package com.dimeng.p2p.signature.fdd.threads;

/*
 * 文 件 名:  ContractPreservationFormationThread.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月23日
 */

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.S62.enums.T6230_F20;
import com.dimeng.p2p.S62.enums.T6273_F07;
import com.dimeng.p2p.S62.enums.T6273_F15;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.signature.fdd.service.FddContractManage;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/**
 *  放款生成PDF合同文档线程类
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [7.1.0, 2017年10月23日]
 * @since 7.1.0
 */
public class FddContractPreservationFormationThread implements Runnable
{
    
    private final ResourceProvider resourceProvider;
    
    private final ConfigureProvider configureProvider;
    
    /**
     * 标的ID
     */
    private final int bidId;
    
    /**
     * 判断线程是否停止
     */
    private boolean stop = false;
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    public FddContractPreservationFormationThread(int bidId)
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
            FddSignatureServiceV25 contractPreservationManage = serviceSession.getService(FddSignatureServiceV25.class);
            FddContractManage contractManage = serviceSession.getService(FddContractManage.class);
            PdfFormationExecutor pdfFormationExecutor = resourceProvider.getResource(PdfFormationExecutor.class);
            
            try
            {
                T6230 t6230 = contractPreservationManage.selectT6230(bidId);
                
                if (T6230_F20.HKZ != t6230.F20)
                {
                    throw new LogicalException("标的不是还款中状态，不能签名，请先放款");
                }
                List<T6273> t6273s = contractPreservationManage.selectT6273ByDidId(bidId);
                if (null == t6273s || t6273s.size() <= 0)
                {
                    contractPreservationManage.insertT6273(bidId);
                    t6273s = contractPreservationManage.selectT6273ByDidId(bidId);
                }
                /*web工程路径*/
                /*    StringBuffer webUrl = new StringBuffer();
                    webUrl.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                        .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                        .append("/console/");*/
                StringBuffer webUrl = new StringBuffer();
                webUrl.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                    .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                    .append(resourceProvider.getContextPath())
                    .append("/");
                Dzxy dzxy = contractManage.getBidContent(bidId);
                if (dzxy == null || (dzxy != null && StringHelper.isEmpty(dzxy.content)))
                {
                    logger.info("获取电子合同模版为空,bidId=" + bidId);
                    /*设置停止线程执行*/
                    setStop();
                }
                
                /*合同头部和尾部*/
                StringBuffer sb = new StringBuffer();
                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER))
                    .append(dzxy.content)
                    .append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                
                for (T6273 t6273 : t6273s)
                {
                    if (stop)
                    {
                        break;
                    }
                    if (t6273 == null)
                    {
                        logger.info("获取t6273失败!,bidId=" + bidId);
                        continue;
                    }
                    //如果签名信息是已签名，或状态为待归档，已归档就不要再次生成合同了
                    if (t6273.F07.name().equals(T6273_F07.YQ) || t6273.F15.name().equals(T6273_F15.DGD.name())
                        || t6273.F15.name().equals(T6273_F15.YGD.name()))
                    {
                        logger.info("获取t6273失败!,bidId=" + bidId + " , t6273.F01=" + t6273.F01 + " ，状态不是未签名状态，不能重复签名");
                        continue;
                    }
                    Map<String, Object> valueMap = contractManage.getValueMap(t6273.F03, t6273.F02, t6273.F04);
                    // t6273.F04 = (String)valueMap.get("xy_no");
                    
                    /*转换PDF合同前生成HTML临时文件*/
                    String path =
                        pdfFormationExecutor.createHTML(valueMap, "", dzxy.xymc, sb.toString(), charset, t6273.F04);
                    /* String path =
                         pdfFormationExecutor.createHTML(valueMap,
                             "contract",
                             dzxy.xymc,
                             sb.toString(),
                             charset,
                             t6273.F04);*/
                    logger.info("生成合同，获取合同更新 = t6273.F04 = " + t6273.F04);
                    /*根据生成html的路径来生成PDF文件*/
                    if (!StringHelper.isEmpty(path))
                    {
                        t6273.F09 = pdfFormationExecutor.convertHtml2Pdf(path, webUrl.toString(), charset);
                        contractManage.updateT6273PdfPathNo(t6273);
                        logger.info("生成pdf合同文档成功！,bidId=" + bidId);
                    }
                }
            }
            catch (Throwable e)
            {
                logger.error("标的生成合同失败，bidID = " + bidId, e);
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
