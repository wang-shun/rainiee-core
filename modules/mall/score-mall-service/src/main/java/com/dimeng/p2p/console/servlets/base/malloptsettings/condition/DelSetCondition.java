package com.dimeng.p2p.console.servlets.base.malloptsettings.condition;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.AbstractMallServlet;
import com.dimeng.p2p.repeater.score.SetScoreManage;
import com.dimeng.util.parser.IntegerParser;

public class DelSetCondition extends AbstractMallServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
    	 SetScoreManage manage = serviceSession.getService(SetScoreManage.class);
        PrintWriter out = response.getWriter();
        int id = IntegerParser.parse(request.getParameter("t6353Id"));
        manage.delT6353(id);
        
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':1,'msg':'");
        sb.append("sussess" + "'}]");
        out.write(sb.toString());
        return;
    }
    
}
