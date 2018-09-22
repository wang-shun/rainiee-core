package com.dimeng.p2p.console.servlets.spread.hdgl.hdlist;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S63.entities.T6340;
import com.dimeng.p2p.S63.enums.T6340_F04;
import com.dimeng.p2p.S63.enums.T6340_F08;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityManage;
import com.dimeng.util.StringHelper;

/**
 * 
 * @author heluzhu
 *
 */
@Right(id = "P2P_C_SPREAD_HDGL_UPDATESTATUS", name = "上架[下架/作废]", moduleId = "P2P_C_SPREAD_HDGL_HDLIST", order = 4)
public class UpdateStatus extends AbstractSpreadServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        sendRedirect(request, response, getController().getURI(request, SearchHdgl.class));
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        try
        {
            int id =
                StringHelper.isEmpty(request.getParameter("id")) ? 0 : Integer.parseInt(request.getParameter("id"));
            ActivityManage manage = serviceSession.getService(ActivityManage.class);
            if (id <= 0)
            {
                throw new ParameterException("该活动不存在");
            }
            String status = request.getParameter("status");
            if (T6340_F08.JXZ.name().equals(status))
            {
                T6340 act = manage.getActivity(id);
                if (T6340_F04.exchange != act.F04 && T6340_F04.integraldraw != act.F04 && new Date().before(act.F06))
                {
                    status = T6340_F08.YSJ.name();
                }
            }
            manage.updateStatus(id, status);
            sendRedirect(request, response, getController().getURI(request, SearchHdgl.class));
        }
        catch (Throwable throwable)
        {
            logger.error(throwable, throwable);
            if (throwable instanceof ParameterException || throwable instanceof LogicalException)
            {
                getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
            else
            {
                //super.onThrowable(request, response, throwable);
                getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
                processGet(request, response, serviceSession);
            }
        }
        
    }
}
