package com.dimeng.p2p.app.servlets;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.platinfo.domain.Version;
import com.dimeng.p2p.common.FileUploader;
import com.dimeng.p2p.modules.base.console.service.AppVersionManage;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersion;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo;
import com.dimeng.util.StringHelper;

/**
 * Servlet implementation class DownloadApp
 */
public class DownloadApp extends AbstractAppServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        final String userAgent = request.getHeader("user-agent").toLowerCase();
        if (userAgent.indexOf("micromessenger") > -1)
        {
            request.getRequestDispatcher("downloadApp.jsp").forward(request, response);
        }
        else
        {
            FileUploader fileUploader = new FileUploader(getResourceProvider());
            
            AppVersionManage appVersionManage = serviceSession.getService(AppVersionManage.class);
            AppVersionInfo[] t7180s = appVersionManage.searchAppVersion(new AppVersion()
            {
                @Override
                public int getVerType()
                {
                    int verType = 1;
                    if (userAgent.indexOf("iphone") > -1)
                    {
                        verType = 2;
                    }
                    return verType;
                }
                
                @Override
                public String getVerNO()
                {
                    return null;
                }
                
                @Override
                public int getStatus()
                {
                    // 0:未使用；1:使用中
                    return 1;
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
                    return true;
                }
                
                @Override
                public int getIsMustUpdate()
                {
                    return -1;
                }
                
                @Override
                public Timestamp getStartPublishDate()
                {
                    return null;
                }
                
                @Override
                public Timestamp getEndPublishDate()
                {
                    return null;
                }
                
                @Override
                public String getPublisher()
                {
                    return null;
                }
                
            }, -1);
            
            String SITE_DOMAIN = getSiteDomain(null);
            
            List<Version> versions = new ArrayList<Version>();
            if (t7180s != null && t7180s.length > 0)
            {
                for (AppVersionInfo t7180 : t7180s)
                {
                    Version version = new Version();
                    version.setId(t7180.id);
                    version.setIsMust(t7180.isMustUpdate);
                    version.setMark(t7180.mark);
                    String fileUrl = fileUploader.getURL(t7180.file);
                    version.setLocalUrl((SITE_DOMAIN + fileUrl));
                    version.setUrl(StringHelper.isEmpty(t7180.url) ? (SITE_DOMAIN + fileUrl) : t7180.url);
                    version.setVerNO(t7180.verNO);
                    version.setVerType(t7180.verType);
                    versions.add(version);
                }
            }
            if (versions.size() > 0)
            {
                Version v = versions.get(0);
                response.sendRedirect(v.getUrl());
            }
            else
            {
                request.getRequestDispatcher("noapp.jsp").forward(request, response);
            }
        }
    }
}
