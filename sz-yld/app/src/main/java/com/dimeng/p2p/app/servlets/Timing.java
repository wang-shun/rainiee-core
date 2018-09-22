package com.dimeng.p2p.app.servlets;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.app.servlets.platinfo.domain.Version;
import com.dimeng.p2p.common.FileUploader;
import com.dimeng.p2p.modules.base.console.service.AppVersionManage;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersion;
import com.dimeng.p2p.modules.base.console.service.entity.AppVersionInfo;
import com.dimeng.p2p.variables.defines.SystemVariable;
import com.dimeng.util.StringHelper;

/**
 * 客户端检查session
 * 
 * @author tanhui
 * 
 */
public class Timing extends AbstractAppServlet
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
        boolean result = false;
        Session session = serviceSession.getSession();
        if (session != null && session.isAuthenticated())
        {
            result = true;
        }
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        AppVersionManage appVersionManage = serviceSession.getService(AppVersionManage.class);
        final String verType = getParameter(request, "verType");
        AppVersionInfo[] t7180s = appVersionManage.searchAppVersion(new AppVersion()
        {
            
            @Override
            public int getVerType()
            {
                if (StringHelper.isEmpty(verType))
                {
                    return 1;
                }
                else
                {
                    return Integer.parseInt(verType);
                }
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
            FileUploader fileUploader = new FileUploader(getResourceProvider());
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
                break;
            }
        }
        
        final ConfigureProvider configureProvider = getConfigureProvider();
        map.put("result", result);
        map.put("time", new Date().getTime());
        map.put("version", versions);
        map.put("siteDomain", configureProvider.format(SystemVariable.SITE_DOMAIN));
        map.put("tel", configureProvider.format(SystemVariable.SITE_CUSTOMERSERVICE_TEL));
        map.put("workTime", configureProvider.format(SystemVariable.SITE_WORK_TIME));
        map.put("filingNum", configureProvider.format(SystemVariable.SITE_FILING_NUM));
        map.put("siteName", configureProvider.format(SystemVariable.SITE_NAME));
        map.put("kfQQ", getConfigureProvider().format(SystemVariable.KFQQ));
        
        // 封装返回信息
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success", map);
        return;
    }
}
