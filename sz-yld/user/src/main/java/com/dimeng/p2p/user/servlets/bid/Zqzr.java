package com.dimeng.p2p.user.servlets.bid;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.codec.digest.UnixCrypt;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S62.entities.T6271;
import com.dimeng.p2p.account.user.service.SafetyManage;
import com.dimeng.p2p.account.user.service.entity.Safety;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.modules.bid.user.service.TenderTransferManage;
import com.dimeng.p2p.order.PdfFormationExecutor;
import com.dimeng.p2p.order.PreservationExecutor;
import com.dimeng.p2p.order.TenderExchangeExecutor;
import com.dimeng.p2p.service.ContractManage;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.p2p.variables.defines.pays.PayVariavle;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

public class Zqzr extends AbstractBidServlet
{
    
    private static final long serialVersionUID = 904758214711922809L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            throw new LogicalException("请不要重复提交请求！");
        }
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        int zcbId = IntegerParser.parse(request.getParameter("zqzrId"));
        int zqId = IntegerParser.parse(request.getParameter("zqId"));
        try
        {
            TenderTransferManage transferManage = serviceSession.getService(TenderTransferManage.class);
            Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
            Boolean isOpenWsd =
                BooleanParser.parseObject(configureProvider.getProperty(PayVariavle.CHARGE_MUST_WITHDRAWPSD));
            if (isOpenWsd)
            {
                String tranPwd = request.getParameter("tranPwd");
                tranPwd = RSAUtils.decryptStringByJs(tranPwd);
                tranPwd = UnixCrypt.crypt(tranPwd, DigestUtils.sha256Hex(tranPwd));
                if (StringHelper.isEmpty(tranPwd))
                {
                    throw new LogicalException("输入正确的交易密码！");
                }
                SafetyManage safetyManage = serviceSession.getService(SafetyManage.class);
                Safety sa = safetyManage.get();
                if (!tranPwd.equals(sa.txpassword))
                {
                    throw new LogicalException("输入正确的交易密码！");
                }
            }
            int orderId = transferManage.purchase(zcbId);
            
            TenderExchangeExecutor executor = resourceProvider.getResource(TenderExchangeExecutor.class);
            executor.submit(orderId, null);
            if (!tg || "FUYOU".equals(configureProvider.format(SystemVariable.ESCROW_PREFIX)))
            {
                executor.confirm(orderId, null);
            }
            
            //生成债权转让合同PDF并保全
            if (Boolean.parseBoolean(configureProvider.getProperty(SystemVariable.IS_SAVE_TRANSFER_CONTRACT)))
            {
                try
                {
                    ContractManage manage = serviceSession.getService(ContractManage.class);
                    List<T6271> t6271List = manage.getClaimList(zcbId);
                    if (null != t6271List)
                    {
                        String charset = resourceProvider.getCharset();
                        PdfFormationExecutor cpfe = resourceProvider.getResource(PdfFormationExecutor.class);
                        /*调用第三方合同保全执行器*/
                        PreservationExecutor preservationExecutor =
                            resourceProvider.getResource(PreservationExecutor.class);
                        Map<String, Object> valueMap = null;
                        StringBuffer sb = new StringBuffer();
                        StringBuffer sbs = new StringBuffer();
                        for (T6271 t6271 : t6271List)
                        {
                            valueMap = manage.getClaimContentMap(zcbId, t6271.F02);
                            if (null != valueMap)
                            {
                                sb.setLength(0);
                                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_HEADER));
                                sb.append((String)valueMap.get("dzxy_content"));
                                sb.append(configureProvider.getProperty(SystemVariable.CONTRACT_TEMPLATE_HTML_FOOTER));
                                String path =
                                    cpfe.createHTML(valueMap,
                                        "contract",
                                        (String)valueMap.get("dzxy_xymc"),
                                        sb.toString(),
                                        charset,
                                        (String)valueMap.get("xy_no"));
                                if (!StringHelper.isEmpty(path))
                                {
                                    sbs.setLength(0);
                                    sbs.append(configureProvider.getProperty(SystemVariable.SITE_REQUEST_PROTOCOL))
                                        .append(configureProvider.getProperty(SystemVariable.SITE_DOMAIN))
                                        .append(request.getContextPath())
                                        .append("/");
                                    String pdfPath = cpfe.convertHtml2Pdf(path, sbs.toString(), charset);
                                    t6271.F04 = (String)valueMap.get("xy_no");
                                    t6271.F09 = pdfPath;
                                    manage.updateT6271PdfPathNo(t6271);
                                    logger.info("生成pdf合同文档成功！");
                                    preservationExecutor.contractPreservation(t6271.F01);
                                }
                            }
                        }
                    }
                    
                }
                catch (Exception e)
                {
                    logger.error("Zqzr.processPost()", e);
                }
            }
            transferManage.writeFrontLog(FrontLogType.GMZQ.getName(), "前台购买债权");
            getController().prompt(request, response, PromptLevel.INFO, "恭喜你，购买成功");
            sendRedirect(request, response, configureProvider.format(URLVariable.FINANCING_ZQZR));
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof SQLException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, configureProvider.format(URLVariable.FINANCING_ZQZR_XQ) + zqId
                    + ".html");
            }
            else if (throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, configureProvider.format(URLVariable.FINANCING_ZQZR));
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        getResourceProvider().log(throwable);
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, configureProvider.format(URLVariable.FINANCING_ZQZR));
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, configureProvider.format(URLVariable.FINANCING_ZQZR));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
    
}
