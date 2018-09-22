package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.message.email.EmailSender;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.GrManage;
import com.dimeng.p2p.modules.bid.console.service.BidManage;
import com.dimeng.p2p.variables.defines.EmailVariavle;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_LOAN_CHECK", name = "审核",moduleId="P2P_C_BID_JKGL_LOANMANAGE",order=4)
public class NotThrough extends AbstractBidServlet
{
    
    private static final long serialVersionUID = -268955794485557959L;
    
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
        int loanId = IntegerParser.parse(request.getParameter("loanId"));
        int useId = IntegerParser.parse(request.getParameter("useId"));
        String des = request.getParameter("des");
        BidManage bidManage = serviceSession.getService(BidManage.class);
        bidManage.notThrough(loanId, des);
        
        // 发送邮件
        EmailSender emailSender = serviceSession.getService(EmailSender.class);
        emailSender.send(0,
            "审核不通过",
            String.format(EmailVariavle.JKSHBTG_MAIL_STR.getDescription(), des, ""),
            serviceSession.getService(GrManage.class).findBasicInfo(useId).mailbox);
        
        sendRedirect(request, response, getController().getURI(request, LoanList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request,
                response,
                getController().getURI(request, DetailAnnexWz.class) + "?loanId=" + request.getParameter("loanId")
                    + "&userId=" + request.getParameter("useId"));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
    
}
