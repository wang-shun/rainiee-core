package com.dimeng.p2p.console.servlets.finance.zjgl.fksh;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.bid.console.service.FkshManage;
import com.dimeng.p2p.modules.bid.console.service.TenderCancelManage;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;
import com.dimeng.p2p.order.TenderCancelExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_FINANCE_LOAN", name = "放款审核", moduleId = "P2P_C_FINANCE_ZJGL_FKGL", order = 1)
public class NotLoan extends AbstractFinanceServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response, final ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            TenderCancelManage tenderCancelManage = serviceSession.getService(TenderCancelManage.class);
            int id = IntegerParser.parse(request.getParameter("id"));
            String des = request.getParameter("des");//流标原因
            BidReturn bidReturn = tenderCancelManage.cancel(id, des);
            int[] orderIds = bidReturn.bidOrderIds;
            String experienceOrderIds = bidReturn.experOrderIds;
            // 标的流标，投资金额返还
            if (orderIds != null && orderIds.length > 0)
            {
                int index = 0;
                ResourceProvider resourceProvider = getResourceProvider();
                ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
                FkshManage fkshManage = serviceSession.getService(FkshManage.class);
                Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
                TenderCancelExecutor executor = resourceProvider.getResource(TenderCancelExecutor.class);
                String userIp = request.getRemoteAddr();
                Map<String, String> paraMap = new HashMap<String, String>();
                paraMap.put("des", des);
                paraMap.put("userIp", userIp);
                for (int orderId : orderIds)
                {
                    if (index == orderIds.length - 1)
                    {
                        paraMap.put("experOrderId", experienceOrderIds);
                    }
                    executor.submit(orderId, paraMap);
                    if (!tg)
                    {
                        executor.confirm(orderId, paraMap);
                    }
                    index++;
                }
                fkshManage.writeLog("操作日志", "标的不放款");
            }
            sendRedirect(request, response, getController().getURI(request, FkshList.class));
        }
        catch (Throwable throwable)
        {
            logger.error("标的不放款", throwable);
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, FkshList.class));
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
}
