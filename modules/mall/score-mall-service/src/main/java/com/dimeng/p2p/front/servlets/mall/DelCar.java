package com.dimeng.p2p.front.servlets.mall;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.repeater.score.MallChangeManage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 删除购物车
 * <功能详细描述>
 * 
 * @author  heluzhu
 * @version  [版本号, 2015年12月18日]
 */
public class DelCar extends AbstractMallServlet
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
        String ids = request.getParameter("ids");
        String id[] = ids.split(",");
        List<Integer> idList = new ArrayList<Integer>();
        for(String carId : id){
            idList.add(Integer.parseInt(carId));
        }
        manage.delCar(idList);
        PrintWriter out = response.getWriter();
        out.print("sucess");
        out.flush();
    */}
}
