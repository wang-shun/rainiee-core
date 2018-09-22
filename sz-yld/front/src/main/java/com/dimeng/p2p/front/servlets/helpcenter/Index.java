package com.dimeng.p2p.front.servlets.helpcenter;

import com.dimeng.framework.http.upload.FileStore;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.resource.ResourceRegister;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.front.servlets.gywm.AbstractGywmServlet;
import com.dimeng.p2p.modules.base.front.service.ArticleManage;
import com.dimeng.p2p.modules.base.front.service.entity.QuestionTypeRecord;
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

        ArticleManage manage = serviceSession.getService(ArticleManage.class);
        QuestionTypeRecord[] qtrs = manage.getQuestionTypes(articType, fileStore);

        jsonMap.put("questionObj", qtrs);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
}
