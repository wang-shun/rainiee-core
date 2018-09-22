package com.dimeng.p2p.console.servlets.info.xxpl.sjbg;

import java.sql.Timestamp;
import java.text.DecimalFormat;

import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.http.upload.PartFile;
import com.dimeng.framework.http.upload.UploadFile;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.p2p.S50.entities.T5011_3;
import com.dimeng.p2p.S50.enums.T5011_F02;
import com.dimeng.p2p.common.enums.ArticlePublishStatus;
import com.dimeng.p2p.console.servlets.info.xxpl.AbstractXxplServlet;
import com.dimeng.p2p.modules.base.console.service.ArticleManage;
import com.dimeng.p2p.modules.base.console.service.entity.Article;
import com.dimeng.p2p.modules.base.console.service.entity.ArticleRecord;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.TimestampParser;

/**
 * 
 * <审计报告>
 * <修改>
 * 
 * @author  liulixia
 * @version  [版本号, 2018年2月6日]
 */
@MultipartConfig
@Right(id = "P2P_C_INFO_XXPL_MENU", name = "信息披露", moduleId = "P2P_C_INFO_XXPL", order = 0)
public class UpdateSjbg extends AbstractXxplServlet
{
    
    private static final long serialVersionUID = 1L;
    
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
        T5011_3 info = manage.getFileInfo(id);
        request.setAttribute("record", record);
        request.setAttribute("info", info);
        forwardView(request, response, getClass());
    }
    
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
                    return ArticlePublishStatus.valueOf(request.getParameter("status"));
                }
                
                @Override
                public UploadFile getImage()
                    throws Throwable
                {
                    Part part = request.getPart("image");
                    if (part == null || part.getContentType() == null || part.getSize() == 0)
                    {
                        return null;
                    }
                    return new PartFile(part);
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
            //获取文件信息
            final Part part = request.getPart("file");
            //获取文件名称
            String h = part.getHeader("content-disposition");
            String filename = h.substring(h.lastIndexOf("=") + 2, h.length() - 1);
            String fileFormat = "";
            String fileSizeString = "";
            T5011_3 t5011_3 = null;
            PartFile file = null;
            if (!(filename == null || filename.equals("")))
            {
                //装换文件大小
                long size = part.getSize();
                DecimalFormat df = new DecimalFormat("#.00");
                
                if (size < 1024)
                {
                    fileSizeString = df.format((double)size) + "B";
                }
                else if (size < 1048576)
                {
                    fileSizeString = df.format((double)size / 1024) + "KB";
                }
                else if (size < 1073741824)
                {
                    fileSizeString = df.format((double)size / 1048576) + "M";
                }
                
                fileFormat = filename.substring(0, filename.indexOf("."));
                t5011_3 = new T5011_3();
                t5011_3.F02 = IntegerParser.parse(request.getParameter("id"));
                t5011_3.F04 = fileSizeString;
                t5011_3.F05 = fileFormat;
                file = new PartFile(part);
            }
            ArticleManage manage = serviceSession.getService(ArticleManage.class);
            manage.update(IntegerParser.parse(request.getParameter("id")), T5011_F02.SJBG, article, t5011_3, file);
            sendRedirect(request, response, getController().getURI(request, SearchSjbg.class));
        }
        catch (ParameterException | LogicalException e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
    }
    
}
