package com.dimeng.p2p.console.servlets.bid.lcgl.zqzrgl;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.financial.console.service.CreditorTransferManage;

@Right(id = "P2P_C_BUSI_ZQSHBTG", name = "审核不通过", moduleId = "P2P_C_BID_LCGL_ZQZRGL", order = 2)
public class UnIssueManage extends AbstractBidServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}
	
	@Override
	protected void processPost(final HttpServletRequest request,final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
		CreditorTransferManage creditorTransferManage = serviceSession.getService(CreditorTransferManage.class);
		String ids = request.getParameter("ids");
		creditorTransferManage.shbtg(ids);

		// 重定向
		sendRedirect(request, response, getController().getURI(request, TransferFailList.class));
	}
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof ParameterException || throwable instanceof SQLException)
        {
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, getController().getURI(request, TransferDshList.class));
        }
        else if (throwable instanceof LogicalException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getURI(request, TransferDshList.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
        }
    }
}
