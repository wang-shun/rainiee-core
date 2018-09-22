package com.dimeng.p2p.console.servlets.finance.txgl.shtg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S61.enums.T6130_F09;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.console.servlets.finance.txgl.txcg.Txcg;
import com.dimeng.p2p.console.servlets.finance.txgl.txsb.Txsb;
import com.dimeng.p2p.modules.account.console.service.UserWithdrawalsManage;
import com.dimeng.p2p.modules.account.console.service.entity.UserWithdrawals;
import com.dimeng.p2p.order.WithdrawExecutor;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_FINANCE_CHECKTXGLFK", name = "放款", moduleId = "P2P_C_FINANCE_TXGL_SHTG", order = 1)
public class CheckWithdraw extends AbstractFinanceServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        UserWithdrawalsManage extraction = serviceSession.getService(UserWithdrawalsManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        UserWithdrawals txglRecord = extraction.get(id);
        request.setAttribute("txglRecord", txglRecord);
        super.processGet(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
        {
            getController().prompt(request, response, PromptLevel.WARRING, "请不要重复提交请求！");
            sendRedirect(request, response, getController().getURI(request, Shtg.class));
            return;
        }
        
        UserWithdrawalsManage extraction = serviceSession.getService(UserWithdrawalsManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        String check_reason = request.getParameter("check_reason");
        T6130_F09 status = EnumParser.parse(T6130_F09.class, request.getParameter("status"));
        try
        {
            ResourceProvider resourceProvider = getResourceProvider();
            ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
            Boolean tg = BooleanParser.parseObject(configureProvider.getProperty(SystemVariable.SFZJTG));
            String escrow = configureProvider.getProperty(SystemVariable.ESCROW_PREFIX);
            int[] orderIds = extraction.fk(status == T6130_F09.YFK, check_reason, id);
            if (orderIds != null && orderIds.length > 0)
            {
                WithdrawExecutor executor = resourceProvider.getResource(WithdrawExecutor.class);
                if (status == T6130_F09.YFK)
                {
                    for (int orderId : orderIds)
                    {
                        executor.submit(orderId, null);
                        //通联在通联包里执行confirm
                        if (!tg && !"ALLINPAY".equals(escrow))
                        {
                            executor.confirm(orderId, null);
                        }
                    }
                }
            }
            if (status == T6130_F09.YFK)
            {
                sendRedirect(request, response, getController().getURI(request, Txcg.class));
                return;
            }
            else
            {
                sendRedirect(request, response, getController().getURI(request, Txsb.class));
                return;
            }
        }
        catch (Throwable throwable)
        {
            logger.error(throwable, throwable);
            if (throwable instanceof ParameterException || throwable instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
}
