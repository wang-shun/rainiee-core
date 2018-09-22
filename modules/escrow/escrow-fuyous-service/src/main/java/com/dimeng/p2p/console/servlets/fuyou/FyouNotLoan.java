package com.dimeng.p2p.console.servlets.fuyou;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.AbstractFuyouServlet;
import com.dimeng.p2p.escrow.fuyou.executor.FYTenderCancelExecutor;
import com.dimeng.p2p.escrow.fuyou.service.BidManage;
import com.dimeng.p2p.modules.bid.console.service.TenderCancelManage;
import com.dimeng.p2p.modules.bid.console.service.entity.BidReturn;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_FINANCE_LOAN", name = "放款审核", moduleId = "P2P_C_FINANCE_ZJGL_FKGL", order = 1)
public class FyouNotLoan extends AbstractFuyouServlet
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
            logger.info("放款审核不放款——IP:" + request.getRemoteAddr());
            TenderCancelManage tenderCancelManage = serviceSession.getService(TenderCancelManage.class);
            BidManage bidManage = serviceSession.getService(BidManage.class);
            int id = IntegerParser.parse(request.getParameter("id"));
            //流标原因
            String des = request.getParameter("des");
            if (des.length() > 200)
            {
                throw new LogicalException("理由长度超过200字!");
            }
            //校验是否存在放款的记录
            boolean isCheck = bidManage.getCheck(id, "Loan");
            if (isCheck)
            {
                throw new LogicalException("该标已执行部分放款，不能执行流标操作！请继续执行放款操作!");
            }
            BidReturn bidReturn = tenderCancelManage.cancel(id, des);
            int[] orderIds = bidReturn.bidOrderIds;
            String experienceOrderIds = bidReturn.experOrderIds;
            // 标的流标，投资金额返还
            if (orderIds != null && orderIds.length > 0)
            {
                int index = 0;
                ResourceProvider resourceProvider = getResourceProvider();
                FYTenderCancelExecutor executor = resourceProvider.getResource(FYTenderCancelExecutor.class);
                Map<String, String> paraMap = new HashMap<String, String>();
                paraMap.put("des", des);
                // 循环流标
                for (int orderId : orderIds)
                {
                    
                    if (index == orderIds.length - 1)
                    {
                        paraMap.put("experOrderId", experienceOrderIds);
                    }
                    executor.submit(orderId, paraMap);
                    
                    executor.confirm(orderId, paraMap);
                    index++;
                }
            }
            sendRedirect(request, response, "/console/finance/zjgl/fksh/fkshList.htm");
        }
        catch (Throwable throwable)
        {
            if (throwable instanceof LogicalException || throwable instanceof ParameterException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                sendRedirect(request, response, "/console/finance/zjgl/fksh/fkshList.htm");
            }
            else
            {
                super.onThrowable(request, response, throwable);
            }
        }
    }
}
