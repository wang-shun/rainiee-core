package com.dimeng.p2p.front.servlets.zxdt;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.front.servlets.gywm.AbstractGywmServlet;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.p2p.modules.base.front.service.NoticeManage;
import com.dimeng.p2p.modules.base.front.service.entity.T5011_EXT;
import com.dimeng.p2p.modules.base.front.service.entity.T5015_EXT;
import com.dimeng.util.parser.IntegerParser;
import org.codehaus.jackson.map.ObjectMapper;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class Index extends AbstractGywmServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        String articType = request.getParameter("articType");

        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        
        ServletContext application = getServletContext();
        ResourceProvider resourceProvider = ResourceRegister.getResourceProvider(application);
        FileStore fileStore = resourceProvider.getResource(FileStore.class);

        Paging paging = new Paging() {
            @Override
            public int getCurrentPage() {
                return IntegerParser.parse(request.getParameter("currentPage"));
            }

            @Override
            public int getSize() {
                return IntegerParser.parse(request.getParameter("pageSize"));
            }
        };

        if(!"WZGG".equals(articType)) {
            ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
            PagingResult<T5011_EXT> results = articleManage.search_EXT(articType, paging, fileStore);
            String pageStr = rendPaging(results);
            jsonMap.put("pageStr", pageStr);
            jsonMap.put("pageCount", results.getPageCount());
            jsonMap.put("artics", results.getItems());
            jsonMap.put("type", articType);
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }else{
            NoticeManage noticeManage = serviceSession.getService(NoticeManage.class);
            PagingResult<T5015_EXT> results = noticeManage.searchExt(paging);
            String pageStr = rendPaging(results);
            jsonMap.put("pageStr", pageStr);
            jsonMap.put("pageCount", results.getPageCount());
            jsonMap.put("artics", results.getItems());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }

    }
    
}
