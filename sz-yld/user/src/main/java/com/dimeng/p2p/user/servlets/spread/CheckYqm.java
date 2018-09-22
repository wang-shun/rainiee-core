package com.dimeng.p2p.user.servlets.spread;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;

import com.dimeng.framework.http.session.authentication.OtherLoginException;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.account.user.service.SpreadManage;

public class CheckYqm extends AbstractSpreadServlet
{
    private static final long serialVersionUID = 1L;
    
    /**
     * 检查邀请码是否存在
     */
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        SpreadManage spreadManage = serviceSession.getService(SpreadManage.class);
        PrintWriter out = response.getWriter();
        String code = request.getParameter("code");
        int userID = serviceSession.getSession().getAccountId();
        int resultCode = spreadManage.checkExistsYqm(code, userID);
        if (resultCode < 0)
        {
            out.println(resultCode);
        }
        else
        {
            spreadManage.updateYqm(code, userID);
            out.println(1);
        }
        out.flush();
    }
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintWriter out = response.getWriter();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        if (throwable instanceof OtherLoginException)
        {
            jsonMap.put("msg", throwable.getMessage());
            out.print(objectMapper.writeValueAsString(jsonMap));
        }
        out.close();
    }
}
