package com.dimeng.p2p.user.servlets.credit;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.enums.FrontLogType;
import com.dimeng.p2p.modules.bid.user.service.TenderRepaymentManage;
import com.dimeng.p2p.order.TenderRepaymentExecutor;
import com.dimeng.p2p.repeater.claim.SubscribeBadClaimManage;
import com.dimeng.p2p.variables.defines.BadClaimVariavle;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.parser.BooleanParser;
import com.dimeng.util.parser.IntegerParser;

public class PaymentOne extends AbstractCreditServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        int id = IntegerParser.parse(request.getParameter("id"));
        int number = IntegerParser.parse(request.getParameter("number"));
        TenderRepaymentManage manage = serviceSession.getService(TenderRepaymentManage.class);
        
        ResourceProvider resourceProvider = getResourceProvider();
        final ConfigureProvider configureProvider = resourceProvider.getResource(ConfigureProvider.class);
        SubscribeBadClaimManage sbcManage = serviceSession.getService(SubscribeBadClaimManage.class);
        Boolean isBadClaim = Boolean.parseBoolean(configureProvider.getProperty(BadClaimVariavle.IS_BADCLAIM_TRANSFER));
        //如果开启了不良债权转让功能
        if (isBadClaim)
        {
            //校验标的是否已被不良债权转让购买
            if (!sbcManage.checkBidBadClaim(id))
            {
                throw new LogicalException("标的已被不良债权转让购买,不能进行还款操作！");
            }
        }
        int[] orderIds = manage.repayment(id, number);
        try
        {
            Boolean tg = BooleanParser.parseObject(getConfigureProvider().getProperty(SystemVariable.SFZJTG));
            TenderRepaymentExecutor executor = getResourceProvider().getResource(TenderRepaymentExecutor.class);
            if (orderIds != null && orderIds.length > 0)
            {
                for (int orderId : orderIds)
                {
                    executor.submit(orderId, null);
                    if (!tg)
                    {
                        executor.confirm(orderId, null);
                    }
                }
                manage.writeFrontLog(FrontLogType.SDHK.getName(), "前台手动还款");
            }
        }
        catch (Throwable e)
        {
            manage.updateT6252(id, number);
            throw e;
        }
        
        request.setAttribute("id", id);
        request.setAttribute("number", number);
        request.setAttribute("orderIds", orderIds);
        
        String url = configureProvider.format(URLVariable.PAY_PAYMENT_URL_SECOND);
        url = url.concat("?id=").concat(id + "");
        forward(request, response, url);
        
        /*sendRedirect(request, response,
        		getController().getViewURI(request, LoanDetail.class) + "?id="
        				+ id);*/
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        int id = IntegerParser.parse(request.getParameter("id"));
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            logger.error(throwable, throwable);
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, getController().getViewURI(request, LoanDetail.class) + "?id=" + id);
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getViewURI(request, LoanDetail.class) + "?id=" + id);
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
        getResourceProvider().log(throwable.getMessage());
    }
}
