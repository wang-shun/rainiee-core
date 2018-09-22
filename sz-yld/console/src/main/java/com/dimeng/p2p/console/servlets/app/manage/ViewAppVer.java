package com.dimeng.p2p.console.servlets.app.manage;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.AppVersionManage;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersion;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_APP_BBGL_UPDATE", name = "修改", moduleId = "P2P_C_APP_BBGL", order = 3)
public class ViewAppVer extends AbstractInformationServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        int id = IntegerParser.parse(request.getParameter("id"));
        if (id <= 0)
        {
            throw new ParameterException("参数错误！");
        }
        
        AppVersionManage manage = serviceSession.getService(AppVersionManage.class);
        
        AppVersionInfo[] result = manage.searchAppVersion(new AppVersion()
        {
            
            @Override
            public int getIsMustUpdate()
            {
                return -1;
            }
            
            @Override
            public int getVerType()
            {
                return -1;
            }
            
            @Override
            public String getVerNO()
            {
                return null;
            }
            
            @Override
            public int getStatus()
            {
                return -1;
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
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Timestamp getEndPublishDate()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public String getPublisher()
            {
                // TODO Auto-generated method stub
                return null;
            }
            
        }, id);
        if (result == null || result.length <= 0)
        {
            throw new ParameterException("没有找到修改的数据");
        }
        request.setAttribute("result", result[0]);
        forwardView(request, response, getClass());
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
        getResourceProvider().log(throwable);
        prompt(request, response, PromptLevel.ERROR, throwable.getMessage());
        
        Controller controller = getController();
        controller.redirectLogin(request, response, controller.getURI(request, AppVerSearch.class));
    }
    
}
