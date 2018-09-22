/*
 * 文 件 名:  DelShoppingCar.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  yuanguangjie
 * 修改时间:  2016年2月23日
 */
package com.dimeng.p2p.app.servlets.user.mall;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppServlet;
import com.dimeng.p2p.app.servlets.platinfo.ExceptionCode;
import com.dimeng.p2p.repeater.score.MallChangeManage;

/**
 * 删除购物车商品
 * 
 * @author  yuanguangjie
 * @version  [版本号, 2016年2月29日]
 */
public class DelShoppingCar extends AbstractAppServlet
{
    private static final long serialVersionUID = 647069847939919002L;
    
    @Override
    protected void processPost(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
    	 MallChangeManage manage = serviceSession.getService(MallChangeManage.class);
    	 //获取要删除的购物车商品Id
         String ids =  getParameter(request, "ids");
         String id[] = ids.split(",");
         List<Integer> idList = new ArrayList<Integer>();
         for(String carId : id){
             idList.add(Integer.parseInt(carId));
         }
         //删除购物车商品
         manage.delCar(idList);
         
        setReturnMsg(request, response, ExceptionCode.SUCCESS, "success!");
        return;
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, final HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        processPost(request, response, serviceSession);
    }
    
}
