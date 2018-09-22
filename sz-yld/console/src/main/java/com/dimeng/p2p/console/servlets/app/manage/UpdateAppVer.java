package com.dimeng.p2p.console.servlets.app.manage;

import java.io.IOException;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S71.entities.T7180;
import com.dimeng.p2p.console.servlets.info.AbstractInformationServlet;
import com.dimeng.p2p.modules.base.console.service.AppVersionManage;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersion;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

@MultipartConfig
@Right(id = "P2P_C_APP_BBGL_UPDATE", name = "修改", moduleId = "P2P_C_APP_BBGL", order = 3)
public class UpdateAppVer extends AbstractInformationServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        AppVersionManage manage = serviceSession.getService(AppVersionManage.class);
        int id = IntegerParser.parse(request.getParameter("F01"));
        final T7180 vinfo = new T7180();
        vinfo.parse(request);
        
        AppVersion appVersion = new AppVersion()
        {
            Part part = request.getPart("file");
            
            @Override
            public int getIsMustUpdate()
            {
                return vinfo.F04;
            }
            
            @Override
            public int getVerType()
            {
                return vinfo.F02;
            }
            
            @Override
            public String getVerNO()
            {
                return vinfo.F03;
            }
            
            @Override
            public int getStatus()
            {
                return vinfo.F07;
            }
            
            @Override
            public String getMark()
            {
                return vinfo.F05;
            }
            
            @Override
            public UploadFile getFile()
                throws Throwable
            {
                if (part == null || part.getContentType() == null || part.getSize() == 0)
                {
                    return null;
                }
                return new PartFile(part);
            }
            
            @Override
            public String getUrl()
            {
                return vinfo.F12;
            }
            
            @Override
            public String getFileName()
            {
                // TODO Auto-generated method stub
                String h = part.getHeader("content-disposition");
                String filename = h.substring(h.lastIndexOf("=") + 2, h.length() - 1);
                return filename;
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
            
        };
        if (id <= 0)
        {
            manage.addAppVersion(appVersion, getResourceProvider());
        }
        else
        {
            manage.updateAppVersion(appVersion, id, getResourceProvider());
        }
        
        sendRedirect(request, response, getController().getURI(request, AppVerSearch.class));
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
        
        request.setAttribute("id", request.getParameter("F01"));
        
        if (StringHelper.isEmpty(request.getParameter("F01")))
        {
            forwardView(request, response, AddAppVer.class);
        }
        else
        {
            AppVersionInfo t7180 = new AppVersionInfo();
            t7180.parse(request);
            request.setAttribute("result", t7180);
            forwardView(request, response, ViewAppVer.class);
        }
    }
    
}
