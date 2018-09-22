/*
 * 文 件 名:  BadClaimContractPreservationThread.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  huqinfu
 * 修改时间:  2016年6月28日
 */
package com.dimeng.p2p.user.servlets.thread;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.S62.enums.T6271_F10;
import com.dimeng.p2p.common.ResourceProviderUtil;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.service.ContractManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/**
 * 
 * <不良债权转让合同保全线程>
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月28日]
 */
public class BadClaimContractPreservationThread implements Runnable
{
    
    private final ResourceProvider resourceProvider;
    
    private final ConfigureProvider configureProvider;
    
    /**
     * 不良债权转让记录id
     */
    private final int blzqzrId;
    
    protected final Logger logger = Logger.getLogger(getClass());
    
    public BadClaimContractPreservationThread(int blzqzrId)
    {
        this.resourceProvider = ResourceProviderUtil.getResourceProvider();
        this.configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        this.blzqzrId = blzqzrId;
    }
    
    @Override
    public void run()
    {
        try (ServiceSession serviceSession = resourceProvider.getResource(ServiceProvider.class).createServiceSession())
        {
            logger.info("执行合同保全线程开始....");
            String charset = resourceProvider.getCharset();
            logger.info("charset=" + charset);
            ContractManage dzxyManage = serviceSession.getService(ContractManage.class);
            try
            {
                Dzxy dzxy = dzxyManage.getBlzqzr(blzqzrId, 0, "");
                String content = dzxy.content;
                String xyName = dzxy.xymc;
                if (dzxy != null)
                {
                    PdfFormationExecutor cpfe = resourceProvider.getResource(PdfFormationExecutor.class);
                    
                    if (!StringHelper.isEmpty(content))
                    {
                        List<T6271> t6271s = dzxyManage.getBadClaimList(blzqzrId);
                        
                        //合同模板
                        StringBuffer sb = new StringBuffer();
                        sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                        sb.append(content);
                        sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                        
                        //加载样式
                        StringBuffer sbs = new StringBuffer();
                        sbs.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                            .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                            .append("/user/");
                        
                        for (T6271 t6271 : t6271s)
                        {
                            //生成购买人合同PDF文档
                            Map<String, Object> valueMap = null;
                            if (t6271.F10 == T6271_F10.ZCR)
                            {
                                valueMap =
                                    dzxyManage.getBadClaimContentMap(blzqzrId, t6271.F11, t6271.F02, "ZCR", "front");
                            }
                            else
                            {
                                valueMap = dzxyManage.getBadClaimContentMap(blzqzrId, 0, t6271.F02, "SRR", "front");
                            }
                            String xyNo = (String)valueMap.get("xy_no");
                            
                            String path = cpfe.createHTML(valueMap, "contract", xyName, sb.toString(), charset, xyNo);
                            if (!StringHelper.isEmpty(path))
                            {
                                path = cpfe.convertHtml2Pdf(path, sbs.toString(), charset);
                                logger.info("生成" + t6271.F10.getChineseName() + "pdf合同文档成功:用户id=" + t6271.F02
                                    + ",不良债权转让id=" + t6271.F12);
                            }
                            
                            T6271 inst6271 = new T6271();
                            inst6271.F01 = t6271.F01;
                            inst6271.F04 = xyNo;
                            inst6271.F09 = path;
                            dzxyManage.updateT6271PdfPathNo(inst6271);
                            
                            //调用第三方进行合同保全
                            //                        PreservationExecutor ebq = resourceProvider.getResource(PreservationExecutor.class);
                            //                        ebq.contractPreservation(t6271.F01);
                            
                        }
                    }
                }
            }
            catch (Throwable e)
            {
                logger.error(e.getMessage());
                e.printStackTrace();
            }
            logger.info("执行合同保全线程结束。");
        }
    }
}
