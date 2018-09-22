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
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.base.console.service.AppStartFindSetManage;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementRecord;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertisementType;

@MultipartConfig
@Right(id = "P2P_C_BASE_UPDATEQDFXYSZ", name = "修改启动发现页图片", moduleId = "P2P_C_APP_BBGL", order = 4)
public class ViewStartFindSet extends AbstractAccountServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
        AppStartFindSetManage appStartFindSetManage = serviceSession.getService(AppStartFindSetManage.class);
        String advType = request.getParameter("advType");
        AdvertisementRecord entity = appStartFindSetManage.selectPic(advType);
        request.setAttribute("entity", entity);
        request.setAttribute("advType", advType);
		forwardView(request, response, getClass());
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
        try
        {
            AppStartFindSetManage appStartFindSetManage = serviceSession.getService(AppStartFindSetManage.class);
            appStartFindSetManage.updateAppStartFindPic(new AdvertisementType()
            {
                
                @Override
                public Timestamp getUnshowTime()
                {
                    return null;
                }
                
                @Override
                public String getURL()
                {
                    return request.getParameter("url");
                }
                
                @Override
                public String getTitle()
                {
                    return request.getParameter("title");
                }
                
                @Override
                public int getSortIndex()
                {
                    return 0;
                }
                
                @Override
                public Timestamp getShowTime()
                {
                    return null;
                }
                
                @Override
                public UploadFile getImage()
                    throws IOException, ServletException
                {
                    Part part = request.getPart("image");
                    if (part == null || part.getContentType() == null || part.getSize() == 0)
                    {
                        return null;
                    }
                    return new PartFile(part);
                }
                
                @Override
                public String getAdvType()
                {
                    return request.getParameter("advType");
                }
            });
            sendRedirect(request, response, getController().getURI(request, AppStartFindSet.class));
        }
        catch (ParameterException | LogicalException e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
	}
}
