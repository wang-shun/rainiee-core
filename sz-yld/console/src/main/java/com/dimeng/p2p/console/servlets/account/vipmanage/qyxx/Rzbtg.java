package com.dimeng.p2p.console.servlets.account.vipmanage.qyxx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.bid.AbstractBidServlet;
import com.dimeng.p2p.modules.account.console.service.QyManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_QYXX_SHLIST", name = "审核", moduleId = "P2P_C_ACCOUNT_QYXX", order = 3)
public class Rzbtg extends AbstractBidServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        QyManage manage = serviceSession.getService(QyManage.class);
        int id = IntegerParser.parse(request.getParameter("yxid"));
        String desc = request.getParameter("desc");
        manage.rzbtg(id, desc);
        forward(request, response, getController().getURI(request, ShList.class));
        
    }
    
}
