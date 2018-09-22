package com.dimeng.p2p.front.servlets.mall;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.repeater.score.MallChangeManage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 
 * 删除收货地址
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月18日]
 */
public class DelAddress extends AbstractMallServlet
{
    private static final long serialVersionUID = -1332501050421123427L;
    
    @Override
    protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        
        processPost(request, response, serviceSession);
    }
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {/*
        PrintWriter out = response.getWriter();
        try {
            MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
            int id = Integer.parseInt(request.getParameter("id"));
            manage.delAddress(id);
            out.print("sucess");
        } catch (Exception e) {
            logger.info(e.getMessage());
            out.print("failed");
        }finally{
            out.close();
        }
    */}
}
