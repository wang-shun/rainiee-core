package com.dimeng.p2p.console.servlets.bid.htgl.agreementSave;

import java.io.File;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6272;
import com.dimeng.p2p.common.entities.Dzxy;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.order.PreservationExecutor;
import com.dimeng.p2p.repeater.preservation.AgreementSaveManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BID_HTGL_XYBQ_SAVE", name = "保全", moduleId = "P2P_C_BID_HTGL_XYBQLB", order = 1)
public class AgreementSaveOperation extends AbstractPreserveServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        try
        {
            final ConfigureProvider configureProvider =
                ResourceRegister.getResourceProvider(getServletContext()).getResource(ConfigureProvider.class);
            AgreementSaveManage agreementSaveManage = serviceSession.getService(AgreementSaveManage.class);
            PreservationExecutor executor = getResourceProvider().getResource(PreservationExecutor.class);
            int id = IntegerParser.parse(request.getParameter("saveId"));
            T6272 t6272 = agreementSaveManage.getAgreementInfo(id);
            String pdfPath = t6272.F07;
            File file = new File(pdfPath);
            if (!file.exists())
            {
                Dzxy dzxy = agreementSaveManage.getSignContent();
                if (dzxy == null)
                {
                    logger.error("网签协议内容为空");
                    return;
                }
                Map<String, Object> valueMap = agreementSaveManage.getValueMap(t6272.F02);
                String xyNo = (String)valueMap.get("xy_no");//协议编号
                String charset = getResourceProvider().getCharset();
                PdfFormationExecutor cpfe = getResourceProvider().getResource(PdfFormationExecutor.class);
                StringBuffer sb = new StringBuffer();
                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                sb.append(dzxy.content);
                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                String path = cpfe.createHTML(valueMap, "agreement", dzxy.xymc, sb.toString(), charset, xyNo);
                String returnPath = "";
                if (!StringHelper.isEmpty(path))
                {
                    StringBuffer sbs = new StringBuffer();
                    sbs.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                        .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                        .append("/console/");
                    returnPath = cpfe.convertHtml2Pdf(path, sbs.toString(), charset);
                    logger.info("生成pdf合同文档成功！");
                    boolean isUpdateOk = agreementSaveManage.updateAgreementContent(id, returnPath);
                    if (isUpdateOk)
                    {
                        executor.agreementPreservation(id);
                    }
                }
            }
            else
            {
                if (id > 0)
                {
                    executor.agreementPreservation(id);
                    logger.info("后台手动保全协议操作完成！");
                }
            }
        }
        catch (SQLException e)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
        }
        catch (LogicalException e)
        {
            logger.error(e, e);
            getController().prompt(request, response, PromptLevel.WARRING, e.getMessage());
        }
        catch (ParameterException e)
        {
            getController().prompt(request, response, PromptLevel.WARRING, e.getMessage());
        }
        finally
        {
            sendRedirect(request, response, getController().getURI(request, AgreementSaveList.class));
        }
        
    }
    
}
