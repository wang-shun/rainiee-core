package com.dimeng.p2p.console.servlets.info.tzgl.xszy;

import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.p2p.modules.base.console.service.entity.Article;
import com.dimeng.p2p.modules.base.console.service.entity.ArticleRecord;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

@Right(id = "P2P_C_INFO_TZGL_MENU", isMenu = true, name = "投资攻略", moduleId = "P2P_C_INFO_TZGL", order = 0)
public class UpdateXszy extends AbstractXszyServlet
{
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ArticleManage manage = serviceSession.getService(ArticleManage.class);
        int id = IntegerParser.parse(request.getParameter("id"));
        ArticleRecord record = manage.get(id);
        if (record == null)
        {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        String connent = manage.getContent(id);
        request.setAttribute("record", record);
        request.setAttribute("connent", connent);
        forwardView(request, response, getClass());
    }
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable
    {
        try
        {
            Article article = new Article()
            {
                
                @Override
                public String getTitle()
                {
                    return request.getParameter("title");
                }
                
                @Override
                public String getSummary()
                {
                    return request.getParameter("summary");
                }
                
                @Override
                public String getSource()
                {
                    return request.getParameter("source");
                }
                
                @Override
                public int getSortIndex()
                {
                    return IntegerParser.parse(request.getParameter("sortIndex"));
                }
                
                @Override
                public ArticlePublishStatus getPublishStatus()
                {
                    return ArticlePublishStatus.YFB;
                }
                
                @Override
                public UploadFile getImage()
                    throws Throwable
                {
                    /*Part part = request.getPart("image");
                    if (part == null || part.getContentType() == null
                    		|| part.getSize() == 0) {
                    	return null;
                    }
                    return new PartFile(part);*/
                    return null;
                }
                
                @Override
                public String getContent()
                {
                    return getResourceProvider().getResource(FileStore.class).encode(request.getParameter("content"));
                }
                
                @Override
                public Timestamp publishTime()
                {
                    return TimestampParser.parse(request.getParameter("publishTime"));
                }
            };
            ArticleManage manage = serviceSession.getService(ArticleManage.class);
            manage.update(IntegerParser.parse(request.getParameter("id")), article);
            sendRedirect(request, response, getController().getURI(request, SearchXszy.class));
        }
        catch (ParameterException | LogicalException e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
    }
    
}
