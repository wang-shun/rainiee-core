package com.dimeng.p2p.front.servlets.mall;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.session.authentication.AuthenticationException;
import com.dimeng.framework.resource.ResourceProvider;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.repeater.score.MallChangeManage;
import com.dimeng.p2p.variables.defines.URLVariable;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * 
 * 添加商品到购物车
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月18日]
 */
public class AddCar extends AbstractMallServlet
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
        
        MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
        int id = Integer.parseInt(request.getParameter("id"));
        int num = IntegerParser.parse(request.getParameter("num"));
        PrintWriter out = response.getWriter();
        if(num <= 0){
            out.write("[{'num':0,'msg':'购买数量输入错误'}]");
            out.close();
            return;
        }
        manage.editCar(id, num);

        out.write("[{'num':1,'msg':'添加成功'}]");
        out.close();
    */}
    
    @Override
    protected void onThrowable(HttpServletRequest request, HttpServletResponse response, Throwable throwable)
        throws ServletException, IOException
    {/*
        ResourceProvider resourceProvider = getResourceProvider();
        resourceProvider.log(throwable);
        PrintWriter out = response.getWriter();
        StringBuilder sb = new StringBuilder();
        sb.append("[{'num':0,'msg':'");
        sb.append(throwable.getMessage() + "'}]");
        out.write(sb.toString());
        if (throwable instanceof AuthenticationException)
        {
            final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
            sendRedirect(request, response, configureProvider.format(URLVariable.LOGIN));
        }
        
    */}
}
