package com.dimeng.p2p.console.servlets.account.vipmanage.qyxx;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.QyManage;

@Right(id = "P2P_C_ACCOUNT_QYVIEW", name = "查看", moduleId = "P2P_C_ACCOUNT_QYXX", order = 1)
public class Open extends AbstractAccountServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        String id = request.getParameter("id");
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        QyManage manage = serviceSession.getService(QyManage.class);
        String[] fileCodes = manage.getFileCodes(Integer.parseInt(id));
        jsonMap.put("fileCodes", fileCodes);
        PrintWriter out = response.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
    
}
