package com.dimeng.p2p.front.servlets.xxpl;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5011_3;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.p2p.modules.base.front.service.entity.T5011_3_EXT;
import com.dimeng.util.parser.IntegerParser;

/**
 * 
 * <信息披露>
 * <功能详细描述>
 * 
 * @author  liulixia
 * @version  [版本号, 2018年3月15日]
 */
public class Yybg extends AbstractXxplServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        ServletContext application = getServletContext();
        ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(application);
        FileStore fileStore = resourceProvider.getResource(FileStore.class);
        
        String type = request.getParameter("type");
        if ("year".equals(type))//获取最近3条年份
        {
            String[] years = articleManage.getYear();
            jsonMap.put("years", years);
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }
        else if ("list".equals(type))
        {
            Paging paging = new Paging()
            {
                @Override
                public int getCurrentPage()
                {
                    return IntegerParser.parse(request.getParameter("currentPage"));
                }
                
                @Override
                public int getSize()
                {
                    return IntegerParser.parse(request.getParameter("pageSize"));
                }
            };
            String year = request.getParameter("year");
            PagingResult<T5011_3_EXT> results = articleManage.searchInformation("YYBG", year, paging, fileStore);
            String pageStr = rendPaging(results);
            jsonMap.put("pageStr", pageStr);
            jsonMap.put("pageCount", results.getPageCount());
            jsonMap.put("yybgs", results.getItems());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }
        else if ("info".equals(type))
        {
            String id = request.getParameter("id");
            T5011_3 info = articleManage.getFileInfo(id);
            jsonMap.put("info", info);
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }
        
    }
}
