package com.dimeng.p2p.user.servlets.capital;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.session.authentication.OtherLoginException;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.account.user.service.ZjlsManage;
import com.dimeng.p2p.account.user.service.entity.CapitalLs;
import com.dimeng.p2p.user.servlets.Login;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.IntegerParser;

public class TradingRecordExport extends AbstractCapitalServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ZjlsManage manage = serviceSession.getService(ZjlsManage.class);
        response.setHeader("Content-disposition",
            "attachment;filename=" + new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
        response.setContentType("application/csv");
        int tradingType = IntegerParser.parse(request.getParameter("tradingType"));
        Date startTime = DateParser.parse(request.getParameter("startTime"));
        Date endTime = DateParser.parse(request.getParameter("endTime"));
        String zhlx =
            StringHelper.isEmpty(request.getParameter("accountType"))
                || "undefined".equals(request.getParameter("accountType")) ? "WLZH"
                : request.getParameter("accountType");
        T6101_F03 f03 = null;
        if (!StringHelper.isEmpty(zhlx))
        {
            f03 = T6101_F03.valueOf(zhlx);
        }
        else
        {
            f03 = T6101_F03.WLZH;
        }
        Paging paging = new Paging()
        {
            
            @Override
            public int getSize()
            {
                return Integer.MAX_VALUE;
            }
            
            @Override
            public int getCurrentPage()
            {
                return 1;
            }
        };
        PagingResult<CapitalLs> tradingRecords = manage.searchLs(tradingType, startTime, endTime, f03, paging);
        manage.export(tradingRecords.getItems(), response.getOutputStream(), "");
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ResourceProvider resourceProvider = getResourceProvider();
        getController().prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        if (throwable instanceof OtherLoginException)
        {
            Controller controller = getController();
            forward(request, response, controller.getViewURI(request, Login.class));
        }
        else
        {
            resourceProvider.log(throwable);
            forwardView(request, response, getClass());
        }
    }
}
