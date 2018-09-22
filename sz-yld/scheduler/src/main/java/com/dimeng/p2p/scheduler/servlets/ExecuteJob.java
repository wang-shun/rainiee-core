/*
 * 文 件 名:  ExecuteJob.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  heluzhu
 * 修改时间: 2016/3/22
 */
package com.dimeng.p2p.scheduler.servlets;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.scheduler.core.SchedulerContainer;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.IntegerParser;

public class ExecuteJob extends AbstractSchedulerServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 2345220271252405110L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        Map<String, String> result = SchedulerContainer.handExecuteJob(IntegerParser.parse(request.getParameter("id")));
        try(PrintWriter out = response.getWriter()) {
            if (!StringHelper.isEmpty(result.get("ERROR"))) {
                out.print("{code:1,msg:'" + result.get("ERROR") + "'}");
            } else {
                out.print("{code:0,msg:'" + result.get("SUCCESS") + "'}");
            }
        }
    }
}
