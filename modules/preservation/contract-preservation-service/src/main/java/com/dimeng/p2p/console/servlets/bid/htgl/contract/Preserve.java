/*
 * 文 件 名:  Preserve.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2016年6月25日
 */
package com.dimeng.p2p.console.servlets.bid.htgl.contract;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.S62.enums.T6271_F08;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.order.PreservationExecutor;
import com.dimeng.p2p.repeater.preservation.ContractPreservationManage;
import com.dimeng.p2p.service.ContractManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/** 
 * <一句话功能简述> 合同保全操作
 * <功能详细描述> 操作栏点击“保全”，调用创建合同保全API（如果PDF电子合同文件未生成，首先生成合同文件）上传电子合同文件给第三方 
 *            保全，返回保全结果时重新刷新页面。结果返回前，禁止点击“一键保全”按钮。如果重复点击“保全”操作。给出重复点击的提示。
 *            如果保全成功的电子合同，保全状态修改为“已保全”，操作变为“查看“。如果保全失败，可以重新点击“保全”操作。
 * @author  xiaoqi
 * @version  [7.2.0, 2016年6月25日]
 */
@Right(id = "P2P_C_BID_HTGL_HTBQ_PRESERVE", name = "保全", moduleId = "P2P_C_BID_HTGL_HTBQLB", order = 1)
public class Preserve extends AbstractPreserveServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = -639857756346874794L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        this.processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int id = Integer.parseInt(request.getParameter("id"));
        ResourceProvider resourceProvider = getResourceProvider();
        ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        ContractPreservationManage contractPreservationManage =
            serviceSession.getService(ContractPreservationManage.class);
        T6271 t6271 = contractPreservationManage.getT6271(id);
        PdfFormationExecutor pdfFormationExecutor = resourceProvider.getResource(PdfFormationExecutor.class);
        String charset = resourceProvider.getCharset();
        ContractManage contractManage = serviceSession.getService(ContractManage.class);
        
        /*没有生成合同pdf文档则生成pdf文档*/
        if (t6271 != null)
        {
            /*没有生成合同pdf文档则先生成pdf文档*/
            if (StringHelper.isEmpty(t6271.F09))
            {
                String path = null;
                Map<String, Object> valueMap = null;
                Dzxy dzxy = null;
                StringBuffer sb = new StringBuffer();
                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                if (T6271_F08.JKHT == t6271.F08)
                {
                    dzxy = contractManage.getBidContent(t6271.F03);
                    if (dzxy == null || (dzxy != null && StringHelper.isEmpty(dzxy.content)))
                    {
                        getController().prompt(request, response, PromptLevel.INFO, "没有配置合同模版或获取模版失败");
                        sendRedirect(request, response, getController().getURI(request, ContractPreservationList.class));
                        return;
                    }
                    valueMap = contractManage.getValueMap(t6271.F03, t6271.F02);
                    t6271.F04 = (String)valueMap.get("xy_no");
                }
                else if (T6271_F08.BLZQZRHT == t6271.F08)
                {
                    dzxy = contractManage.getBlzqzr(t6271.F12, 0, "console");
                    if (dzxy == null || (dzxy != null && StringHelper.isEmpty(dzxy.content)))
                    {
                        getController().prompt(request, response, PromptLevel.INFO, "没有配置合同模版或获取模版失败");
                        sendRedirect(request, response, getController().getURI(request, ContractPreservationList.class));
                        return;
                    }
                    valueMap =
                        contractManage.getBadClaimContentMap(t6271.F12,
                            t6271.F11,
                            t6271.F02,
                            t6271.F10.name(),
                            "console");
                    t6271.F04 = (String)valueMap.get("xy_no");
                }
                else if (T6271_F08.DFHT == t6271.F08)
                {
                    dzxy = new Dzxy();
                    valueMap = contractManage.getAdvanceContentMap(t6271.F03, t6271.F02);
                    if (null == valueMap || null == valueMap.get("dzxy_content")
                        || StringHelper.isEmpty((String)valueMap.get("dzxy_content")))
                    {
                        getController().prompt(request, response, PromptLevel.INFO, "没有配置合同模版或获取模版失败");
                        sendRedirect(request, response, getController().getURI(request, ContractPreservationList.class));
                        return;
                    }
                    dzxy.content = (String)valueMap.get("dzxy_content");
                    dzxy.xymc = (String)valueMap.get("dzxy_xymc");
                    t6271.F04 = (String)valueMap.get("xy_no");
                }
                else if (T6271_F08.ZQZRHT == t6271.F08)
                {
                    dzxy = new Dzxy();
                    valueMap = contractManage.getClaimContentMap(t6271.F13, t6271.F02);
                    if (null == valueMap || null == valueMap.get("dzxy_content")
                        || StringHelper.isEmpty((String)valueMap.get("dzxy_content")))
                    {
                        getController().prompt(request, response, PromptLevel.INFO, "没有配置合同模版或获取模版失败");
                        sendRedirect(request, response, getController().getURI(request, ContractPreservationList.class));
                        return;
                    }
                    dzxy.content = (String)valueMap.get("dzxy_content");
                    dzxy.xymc = (String)valueMap.get("dzxy_xymc");
                    t6271.F04 = (String)valueMap.get("xy_no");
                }
                else
                {
                    logger.info("无法获取到" + t6271.F08.getChineseName() + "类型的合同模板！");
                    getController().prompt(request,
                        response,
                        PromptLevel.INFO,
                        "无法获取到" + t6271.F08.getChineseName() + "类型的合同模板！");
                    sendRedirect(request, response, getController().getURI(request, ContractPreservationList.class));
                    return;
                }
                sb.append(dzxy.content);
                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                path =
                    pdfFormationExecutor.createHTML(valueMap, "contract", dzxy.xymc, sb.toString(), charset, t6271.F04);
                
                if (!StringHelper.isEmpty(path))
                {
                    StringBuffer sbs = new StringBuffer();
                    sbs.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                        .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                        .append(request.getContextPath())
                        .append("/");
                    t6271.F09 = pdfFormationExecutor.convertHtml2Pdf(path, sbs.toString(), charset);
                    contractManage.updateT6271PdfPathNo(t6271);
                    logger.info("生成pdf合同文档成功！");
                }
            }
            
            /*调用第三方合同保全执行器*/
            PreservationExecutor preservationExecutor = resourceProvider.getResource(PreservationExecutor.class);
            preservationExecutor.contractPreservation(t6271.F01);
            contractPreservationManage.writeLog("操作日志", "用户执行保全操作");
        }
        //getController().prompt(request, response, PromptLevel.INFO, "合同保成功");
        sendRedirect(request, response, getController().getURI(request, ContractPreservationList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        super.onThrowable(request, response, throwable);
    }
    
}
