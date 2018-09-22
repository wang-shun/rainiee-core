package com.dimeng.p2p.front.servlets;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.common.FormToken;

/**
 * 通过ajax生成tonken值隐藏域
 * 
 * @author  huqinfu
 * @version  [版本号, 2016年6月23日]
 */
public class GetTokenForAjax extends AbstractFrontServlet
{
    
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        String token = FormToken.hidden(serviceSession.getSession());
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("token", token);
        out.print(objectMapper.writeValueAsString(jsonMap));
        out.close();
    }
}
