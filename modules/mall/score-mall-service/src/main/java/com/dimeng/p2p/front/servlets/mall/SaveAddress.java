package com.dimeng.p2p.front.servlets.mall;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S63.entities.T6355;
import com.dimeng.p2p.repeater.score.MallChangeManage;

/**
 * 
 * 保存收货地址
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月18日]
 */
public class SaveAddress extends AbstractMallServlet
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
        try
        {
            MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
            T6355 t6355 = new T6355();
            t6355.parse(request);
            manage.editAddress(t6355);
            out.print("sucess");
        }
        catch (Exception e)
        {
            out.print(e.getMessage());
        }
        finally
        {
            out.close();
        }
    */}
}
