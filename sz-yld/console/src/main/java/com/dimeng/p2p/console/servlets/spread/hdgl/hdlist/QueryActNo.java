package com.dimeng.p2p.console.servlets.spread.hdgl.hdlist;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.spread.AbstractSpreadServlet;
import com.dimeng.p2p.modules.activity.console.service.ActivityManage;

/**
 * 
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年10月10日]
 */
public class QueryActNo extends AbstractSpreadServlet
{
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        ActivityManage manage = serviceSession.getService(ActivityManage.class);
        PrintWriter out = response.getWriter();
        String jlType = request.getParameter("jlType");
        String hdType = request.getParameter("hdType");
        out.write("{result:'"+manage.queryExistNo(jlType, hdType)+"'}");
        return;
    }
}
