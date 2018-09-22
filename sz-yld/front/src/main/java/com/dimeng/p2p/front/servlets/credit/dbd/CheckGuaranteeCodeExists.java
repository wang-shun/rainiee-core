package com.dimeng.p2p.front.servlets.credit.dbd;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.front.servlets.Register;
import com.dimeng.p2p.front.servlets.credit.AbstractCreditServlet;
import com.dimeng.p2p.modules.bid.front.service.BidManage;
import com.dimeng.p2p.repeater.guarantor.ApplyGuarantorManage;
import com.dimeng.util.StringHelper;

public class CheckGuaranteeCodeExists extends AbstractCreditServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected boolean mustAuthenticated()
    {
        return false;
    }
    
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
        response.setContentType("text/html;charset=" + getResourceProvider().getCharset());
        String gCode = request.getParameter("gCode");
        if (StringHelper.isEmpty(gCode))
        {
            response.getWriter().println(false);
            response.getWriter().flush();
            return;
        }
        boolean isLegalGuarantee = false;
        ApplyGuarantorManage applyGuarantorManage = serviceSession.getService(ApplyGuarantorManage.class);
        int guarantorId = applyGuarantorManage.getGuarantId(gCode, true);
        if (guarantorId > 0 && guarantorId != serviceSession.getSession().getAccountId())
        {
            BidManage bidManage = serviceSession.getService(BidManage.class);
            if (!bidManage.isUserHasYQ(guarantorId))
            {
                isLegalGuarantee = true;
            }
        }
        response.getWriter().println(isLegalGuarantee);
        response.getWriter().flush();
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        if (throwable instanceof SQLException || throwable instanceof ParameterException
            || throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
            throwable.getStackTrace();
            sendRedirect(request, response, getController().getViewURI(request, Register.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
