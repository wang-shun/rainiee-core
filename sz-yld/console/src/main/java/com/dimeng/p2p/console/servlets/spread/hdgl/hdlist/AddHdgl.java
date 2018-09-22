package com.dimeng.p2p.console.servlets.spread.hdgl.hdlist;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.S63.enums.T6340_F03;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityManage;
import com.dimeng.p2p.modules.activity.console.service.entity.Activity;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_SPREAD_HDGL_ADDHDGL", name = "新增", moduleId = "P2P_C_SPREAD_HDGL_HDLIST", order = 1)
public class AddHdgl extends AbstractSpreadServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        //控制每种奖励类型只能开展对应的活动类型
        String hdType = request.getParameter("hdType"); //活动类型
        String jlType = request.getParameter("jlType"); //奖励类型
       /* if(T6340_F03.experience.name().equals(jlType)){
            //体验金：指定用户赠送、注册赠送
            if(!T6340_F04.register.name().equals(hdType) && !T6340_F04.foruser.name().equals(hdType)){
                sendRedirect(request, response, getController().getURI(request, ChooseType.class));
                return;
            }
        }*/
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            if (!FormToken.verify(serviceSession.getSession(), request.getParameter(FormToken.parameterName())))
            {
                throw new LogicalException("请不要重复提交请求！");
            }
            ActivityManage manage = serviceSession.getService(ActivityManage.class);
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
            manage.addActivity(activity);
            sendRedirect(request, response, getController().getURI(request, SearchHdgl.class));
        }
        catch (Throwable e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            logger.error(e, e);
            processGet(request, response, serviceSession);
        }
    }
    
}
