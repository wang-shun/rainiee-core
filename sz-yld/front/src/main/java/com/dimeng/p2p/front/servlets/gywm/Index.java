package com.dimeng.p2p.front.servlets.gywm;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.p2p.S50.entities.T5013;
import com.dimeng.p2p.modules.base.front.service.PartnerManage;
import com.dimeng.p2p.modules.base.front.service.entity.T5013_EXT;
import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5011;
import com.dimeng.p2p.S61.entities.T6195_EXT;
import com.dimeng.p2p.account.user.service.TzjyManage;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.p2p.modules.base.front.service.entity.T5011_EXT;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class Index extends AbstractGywmServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        
        String articType = request.getParameter("articType");
        ArticleManage articleManage = serviceSession.getService(ArticleManage.class);
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
        
        if ("GSJJ".equals(articType) || "ZXNS".equals(articType) || "LXWM".equals(articType))
        {
            T5011 article = articleManage.get(articType);
            String articleContent = "";
            if (article != null)
            {
                articleManage.view(article.F01);
                articleContent = articleManage.getContent(article.F01);
            }
            
            StringHelper.format(out, articleContent, fileStore);
            out.close();
        }
        
        if ("GLTD".equals(articType) || "ZJGW".equals(articType))
        {
            PagingResult<T5011_EXT> results = articleManage.search_EXT(articType, paging, fileStore);
            String pageStr = rendPaging(results);
            jsonMap.put("pageStr", pageStr);
            jsonMap.put("pageCount", results.getPageCount());
            jsonMap.put("artics", results.getItems());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }

        if ("HZHB".equals(articType))
        {
            PartnerManage partnerManage = serviceSession.getService(PartnerManage.class);
            PagingResult<T5013_EXT> results = partnerManage.gett5013ExtList(paging, fileStore);
            String pageStr = rendPaging(results);
            jsonMap.put("pageStr", pageStr);
            jsonMap.put("pageCount", results.getPageCount());
            jsonMap.put("artics", results.getItems());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }
        
        if ("YJFK".equals(articType))
        {
            TzjyManage manage = serviceSession.getService(TzjyManage.class);
            PagingResult<T6195_EXT> t6195_EXTs = manage.search(0, paging);
            String pageStr = rendPaging(t6195_EXTs);
            jsonMap.put("pageStr", pageStr);
            jsonMap.put("pageCount", t6195_EXTs.getPageCount());
            jsonMap.put("artics", t6195_EXTs.getItems());
            out.print(objectMapper.writeValueAsString(jsonMap));
            out.close();
        }
    }
    
}
