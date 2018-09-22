package com.dimeng.p2p.front.servlets.mall;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.repeater.score.MallChangeManage;
import com.dimeng.p2p.repeater.score.entity.ShoppingCarResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 
 * 购物车
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月18日]
 */
public class CarList extends AbstractMallServlet
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
        ShoppingCarResult[] result =  manage.queryCar();
        request.setAttribute("result", result);
        forwardView(request, response, getClass());
    */}
}
