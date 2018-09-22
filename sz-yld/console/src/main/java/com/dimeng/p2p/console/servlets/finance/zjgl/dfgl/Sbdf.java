package com.dimeng.p2p.console.servlets.finance.zjgl.dfgl;

import java.util.HashMap;
import java.util.List;
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
import com.dimeng.p2p.modules.bid.console.service.AdvanceManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.order.DFAdvanceExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_FINANCE_SBDF_PTDF", name = "垫付", moduleId = "P2P_C_FINANCE_ZJGL_PTDFGL", order = 1)
public class Sbdf extends AbstractFinanceServlet
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
            BidManage bidManage = serviceSession.getService(BidManage.class);
            int loanId = IntegerParser.parse(request.getParameter("loanId"));
            int period = IntegerParser.parse(request.getParameter("period"));
            ResourceProvider resourceProvider = getResourceProvider();
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            DFAdvanceExecutor advanceExecutor = getResourceProvider().getResource(DFAdvanceExecutor.class);
            Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
            List<Integer> orderIds = bidManage.addOrder(loanId);
            int accountId = serviceSession.getSession().getAccountId();
            Map<String, String> params = new HashMap<String, String>();
            params.put("accountId", String.valueOf(accountId));
            params.put("period", String.valueOf(period));
            
            if (orderIds != null)
            {
                for (Integer orderId : orderIds)
                {
                    advanceExecutor.submit(orderId, params);
                    if (!tg)
                    {
                        advanceExecutor.confirm(orderId, params);
                    }
                }
            }
            //insertT6256
            AdvanceManage advanceManage = serviceSession.getService(AdvanceManage.class);
            advanceManage.insertT6256(loanId);
            sendRedirect(request, response, getController().getURI(request, YdfList.class));
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
                sendRedirect(request, response, getController().getURI(request, YqddfList.class));
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
}
