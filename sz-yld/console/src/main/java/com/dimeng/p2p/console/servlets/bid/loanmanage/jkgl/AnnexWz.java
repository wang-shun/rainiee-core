package com.dimeng.p2p.console.servlets.bid.loanmanage.jkgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6212;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.bid.console.service.AnnexManage;

public class AnnexWz extends AbstractBidServlet
{
    
    /**
     * 
     */
    private static final long serialVersionUID = -268955794485557959L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        AnnexManage annexManage = serviceSession.getService(AnnexManage.class);
        T6212[] annexTypes = annexManage.searchAnnexType();
        request.setAttribute("annexTypes", annexTypes);
        forwardView(request, response, getClass());
    }
}
