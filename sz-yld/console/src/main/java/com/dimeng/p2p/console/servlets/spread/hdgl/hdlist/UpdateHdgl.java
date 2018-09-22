package com.dimeng.p2p.console.servlets.spread.hdgl.hdlist;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S63.entities.T6340;
import com.dimeng.p2p.S63.entities.T6344;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityManage;
import com.dimeng.p2p.modules.activity.console.service.entity.Activity;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_SPREAD_HDGL_UPDATEHDGL", name = "修改", moduleId = "P2P_C_SPREAD_HDGL_HDLIST", order = 3)
public class UpdateHdgl extends AbstractSpreadServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ActivityManage manage = serviceSession.getService(ActivityManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        T6340 activity = manage.getActivity(id);
        T6344[] t6344s = manage.getT6344Arry(id);
        request.setAttribute("activity", activity);
        request.setAttribute("t6344s", t6344s);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            ActivityManage manage = serviceSession.getService(ActivityManage.class);
            int id = IntegerParser.parse(request.getParameter("id"));
            Activity activity = new Activity()
            {
                
                @Override
                public String title()
                {
                    
                    return request.getParameter("title");
                }
                
                @Override
                public String[] syqxType()
                {
                    
                    return request.getParameterValues("syqxType");
                }
                
                @Override
                public Timestamp startTime()
                {
                    return TimestampParser.parse(request.getParameter("startTime"));
                }
                
                @Override
                public String[] money()
                {
                    return request.getParameterValues("jl");
                }
                
                @Override
                public Timestamp endTime()
                {
                    return TimestampParser.parse(request.getParameter("endTime"));
                }
                
                /**
                 *发放数量
                 */
                @Override
                public String[] num()
                {
                    return request.getParameterValues("num");
                }
                
                /**
                 * 最低投资金额
                 */
                @Override
                public String[] leastInvest()
                {
                    return request.getParameterValues("leastInvest");
                }
                
                @Override
                public String[] edu()
                {
                    return request.getParameterValues("zdczed");
                }
                
                @Override
                public String remark()
                {
                    return request.getParameter("remark");
                }
                
                @Override
                public String[] syqx()
                {
                    return request.getParameterValues("syqx");
                }
                
                @Override
                public int sygz()
                {
                    return !StringHelper.isEmpty(request.getParameter("sygz")) ? Integer.parseInt(request.getParameter("sygz"))
                        : 0;
                }
                
                @Override
                public int sfsccz()
                {
                    return !StringHelper.isEmpty(request.getParameter("sfsccz")) ? Integer.parseInt(request.getParameter("sfsccz"))
                        : 1;
                }
                
                @Override
                public String litj()
                {
                    return request.getParameter("lqtj");
                }
                
                @Override
                public String userId()
                {
                    return request.getParameter("userId");
                }
                
                @Override
                public String jlType()
                {
                    return request.getParameter("jlType");
                }
                
                @Override
                public String hdType()
                {
                    return request.getParameter("hdType");
                }

                @Override
                public String validMethod() { return request.getParameter("validMethod");  }

                @Override
                public String validDate() { return request.getParameter("validDate");  }
                
            };
            manage.updateActivity(id, activity);
            sendRedirect(request, response, getController().getURI(request, SearchHdgl.class));
        }
        catch (Exception e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
    }
}
