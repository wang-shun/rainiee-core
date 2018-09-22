package com.dimeng.p2p.console.servlets.spread.hdgl.hdlist;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S63.entities.T6340;
import com.dimeng.p2p.S63.entities.T6344;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_SPREAD_HDGL_SEARCHHDXQ", name = "查看", moduleId = "P2P_C_SPREAD_HDGL_HDLIST", order = 2)
public class SearchHdxq extends AbstractSpreadServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        ActivityManage manage = serviceSession.getService(ActivityManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        T6340 result = manage.getActivity(id);
        T6344[] t6344s = manage.getT6344Arry(id);
        request.setAttribute("t6344s", t6344s);
        request.setAttribute("result", result);
        request.setAttribute("logs", manage.activityLogs(id));
        if (T6340_F03.experience.name().equals(result.F03.name()))
        {
            request.setAttribute("userNames", manage.queryExperienceForUsers(id));
        }
        else
        {
            request.setAttribute("userNames", manage.queryForUsers(id));
        }
        forwardView(request, response, getClass());
    }
    
}
