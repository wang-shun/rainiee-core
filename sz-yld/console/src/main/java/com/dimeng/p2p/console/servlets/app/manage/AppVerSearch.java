package com.dimeng.p2p.console.servlets.app.manage;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.AppVersionManage;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersion;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_APP_BBGL_SEARCH", isMenu = true, name = "版本管理", moduleId = "P2P_C_APP_BBGL", order = 0)
public class AppVerSearch extends AbstractInformationServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        AppVersionManage manage = serviceSession.getService(AppVersionManage.class);
        PagingResult<AppVersionInfo> list = manage.searchAppVersionPaging(new AppVersion()
        {
            
            @Override
            public int getIsMustUpdate()
            {
                return StringHelper.isEmpty(request.getParameter("isMustUpdate")) ? -1
                    : Integer.parseInt(request.getParameter("isMustUpdate"));
            }
            
            @Override
            public int getVerType()
            {
                return StringHelper.isEmpty(request.getParameter("verType")) ? -1
                    : Integer.parseInt(request.getParameter("verType"));
            }
            
            @Override
            public String getVerNO()
            {
                return request.getParameter("verNO");
            }
            
            @Override
            public int getStatus()
            {
                return StringHelper.isEmpty(request.getParameter("status")) ? -1
                    : Integer.parseInt(request.getParameter("status"));
            }
            
            @Override
            public String getMark()
            {
                return null;
            }
            
            @Override
            public UploadFile getFile()
                throws Throwable
            {
                return null;
            }
            
            @Override
            public String getUrl()
            {
                return null;
            }
            
            @Override
            public String getFileName()
            {
                return null;
            }
            
            @Override
            public boolean getLimit()
            {
                return false;
            }
            
            @Override
            public Timestamp getStartPublishDate()
            {
                return TimestampParser.parse(request.getParameter("startPublishDate"));
            }
            
            @Override
            public Timestamp getEndPublishDate()
            {
                return TimestampParser.parse(request.getParameter("endPublishDate"));
            }
            
            @Override
            public String getPublisher()
            {
                return request.getParameter("publisher");
            }
            
        },
            new Paging()
            {
                
                @Override
                public int getSize()
                {
                    return 10;
                }
                
                @Override
                public int getCurrentPage()
                {
                    return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
                }
            });
        request.setAttribute("list", list);
        forwardView(request, response, getClass());
    }
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
