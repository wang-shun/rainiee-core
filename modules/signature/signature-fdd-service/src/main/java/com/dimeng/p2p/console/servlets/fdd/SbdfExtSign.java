/*
 * 文 件 名:  SbdfExtSign.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  guomianyun
 * 修改时间:  2017年10月30日
 */
package com.dimeng.p2p.console.servlets.fdd;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFddServlet;
import com.dimeng.p2p.S62.entities.T6230;
import com.dimeng.p2p.S62.entities.T6273;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.signature.fdd.service.FddContractManage;
import com.dimeng.p2p.signature.fdd.service.FddSignatureServiceV25;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

/**
 * 散标垫付数据
 * <功能详细描述>
 * 
 * @author  guomianyun
 * @version  [版本号, 2017年10月30日]
 */
public class SbdfExtSign extends AbstractFddServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        //标ID
        String loanId = request.getParameter("bidId");
        
        FddSignatureServiceV25 contractPreservationManage = serviceSession.getService(FddSignatureServiceV25.class);
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        int bidId = IntegerParser.parse(loanId);
        T6230 t6230 = contractPreservationManage.selectT6230(bidId);
        if (null != t6230)
        {
            //易保全不使用，使用法大大电子签章
            FddContractManage manage = serviceSession.getService(FddContractManage.class);
            List<T6273> t6273List = manage.getDfList(bidId);
            if ((null == t6273List) || (t6273List.size() <= 0))
            {
                manage.insertT6273DF(bidId);
            }
            t6273List = manage.getDfList(bidId);
            if ((null != t6273List) && (t6273List.size() > 0))
            {
                // 合同内容
                StringBuffer sbContract = new StringBuffer();
                StringBuffer sbContractSave = new StringBuffer();
                int userId = serviceSession.getSession().getAccountId();
                Map<String, Object> valueMap = manage.getAdvanceContentMap(bidId, userId, "");
                if (null != valueMap)
                {
                    PdfFormationExecutor cpfe = resourceProvider.getResource(PdfFormationExecutor.class);
                    String charset = resourceProvider.getCharset();
                    sbContractSave.setLength(0);
                    sbContractSave.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                        .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                        .append(request.getContextPath())
                        .append("/");
                    
                    sbContract.setLength(0);
                    sbContract.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                    sbContract.append((String)valueMap.get("dzxy_content"));
                    sbContract.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                    /*PreservationExecutor preservationExecutor =
                        resourceProvider.getResource(PreservationExecutor.class);*/
                    for (T6273 t6273 : t6273List)
                    {
                        
                        valueMap.put("xy_no", t6273.F04);
                        String dfrPath =
                            cpfe.createHTML(valueMap,
                                "contract",
                                (String)valueMap.get("dzxy_xymc"),
                                sbContract.toString(),
                                charset,
                                (String)valueMap.get("xy_no"));
                        if (!StringHelper.isEmpty(dfrPath))
                        {
                            String dfrContractPath = cpfe.convertHtml2Pdf(dfrPath, sbContractSave.toString(), charset);
                            t6273.F09 = dfrContractPath;
                            manage.updateT6273PdfPathNo(t6273);
                            logger.info("生成垫付pdf合同文档成功！, bid =" + loanId);
                            //调用第三方合同保全执行器
                            //preservationExecutor.contractPreservation(dfrT6271.F01);
                        }
                    }
                }
            }
            else
            {
                logger.info("根据标ID进行垫付补标,标信息不存在 , bid =" + loanId);
            }
        }
    }
    
}
