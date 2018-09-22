package com.dimeng.p2p.app.servlets.pay.service.fuyou;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.bouncycastle.util.encoders.Base64;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.config.Config;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.modules.bid.user.service.TenderTransferManage;
import com.dimeng.p2p.order.TenderExchangeExecutor;
import com.dimeng.util.parser.IntegerParser;

public class Zqzr extends AbstractFuyouServlet
{
    
    private static final long serialVersionUID = 904758214711922809L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        //        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        //        {
        //            throw new LogicalException("请不要重复提交请求！");
        //        }
        ResourceProvider resourceProvider = getResourceProvider();
        
        int zcbId = IntegerParser.parse(request.getParameter("zqzrId"));
        TenderTransferManage transferManage = serviceSession.getService(TenderTransferManage.class);
        int orderId = transferManage.purchase(zcbId);
        
        TenderExchangeExecutor executor = resourceProvider.getResource(TenderExchangeExecutor.class);
        executor.submit(orderId, null);
        executor.confirm(orderId, null);
        transferManage.writeFrontLog(FrontLogType.ZRZQ.getName(), "前台购买债权");
        getController().prompt(request, response, PromptLevel.INFO, "恭喜你，购买成功");
        String url = getSiteDomain(Config.buyCreditorRetUrl) + "?code=000000&description=success";
        sendRedirect(request, response, url);
        return;
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        String enRetUrl = Config.buyCreditorRetUrl;
        String url =
            enRetUrl + "?" + "code=000004&description="
                + URLEncoder.encode(new String(Base64.encode(throwable.getMessage().getBytes("UTF-8")), "UTF-8"));
        
        response.sendRedirect(url);
        getResourceProvider().log(throwable);
    }
    
}
