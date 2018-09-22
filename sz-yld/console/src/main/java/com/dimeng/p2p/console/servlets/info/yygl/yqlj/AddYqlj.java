package com.dimeng.p2p.console.servlets.info.yygl.yqlj;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.FormToken;
import com.dimeng.p2p.modules.base.console.service.FriendlyLinkManage;
import com.dimeng.p2p.modules.base.console.service.entity.FriendlyLink;

@Right(id = "P2P_C_INFO_YYGL_MENU", name = "运营管理", moduleId = "P2P_C_INFO_YYGL", order = 0)
public class AddYqlj extends AbstractYqljServlet
{
    
    private static final long serialVersionUID = 1L;
    
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
            FriendlyLink link = new FriendlyLink()
            {
                
                @Override
                public String getURL()
                {
                    return request.getParameter("url");
                }
                
                @Override
                public int getSortIndex()
                {
                    return Integer.parseInt(request.getParameter("sortIndex"));
                }
                
                @Override
                public String getName()
                {
                    return request.getParameter("name");
                }
            };
            FriendlyLinkManage manage = serviceSession.getService(FriendlyLinkManage.class);
            manage.add(link);
            sendRedirect(request, response, getController().getURI(request, SearchYqlj.class));
        }
        catch (ParameterException | LogicalException e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
        
    }
    
}
