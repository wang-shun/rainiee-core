package com.dimeng.p2p.console.servlets.base.optsettings.dxz;

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
import com.dimeng.p2p.S62.entities.T6217;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.bid.console.service.DxzBidManage;

/**
 * 
 * 新增定向组
 * @author  zhongsai
 * @version  [V7.0, 2018年2月8日]
 */
@Right(id = "P2P_C_BASE_DXZADD", name = "新增定向组", moduleId = "P2P_C_BASE_OPTSETTINGS_DXZTYPE", order = 1)
public class AddDxz extends AbstractBaseServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        DxzBidManage dxzBidManage = serviceSession.getService(DxzBidManage.class);
        T6217 t6217 = new T6217();
        t6217.parse(request);
        dxzBidManage.addT6217(t6217);
        sendRedirect(request, response, getController().getURI(request, DxzList.class));
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        getResourceProvider().log(throwable.getMessage());
        if (throwable instanceof SQLException)
        {
            logger.error(throwable, throwable);
            getController().prompt(request, response, PromptLevel.ERROR, "系统繁忙，请您稍后再试");
            sendRedirect(request, response, getController().getURI(request, AddDxz.class));
            
        }
        else if (throwable instanceof LogicalException || throwable instanceof ParameterException)
        {
            getController().prompt(request, response, PromptLevel.WARRING, throwable.getMessage());
            sendRedirect(request, response, getController().getURI(request, AddDxz.class));
        }
        else
        {
            super.onThrowable(request, response, throwable);
            sendRedirect(request, response, getController().getURI(request, AddDxz.class));
        }
    }
    
}
