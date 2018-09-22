/*
 * 文 件 名:  WeiXinServlet.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  zhoulantao
 * 修改时间:  2016年4月11日
 */
package com.dimeng.p2p.app.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.Controller;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.weixin.AbstractWeiXinServlet;
import com.dimeng.p2p.app.servlets.weixin.GetMsgServlet;
import com.dimeng.p2p.app.servlets.weixin.MsgServlet;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  zhoulantao
 * @version  [版本号, 2016年4月11日]
 */
public class WeiXinServlet extends AbstractWeiXinServlet
{
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 5710839016977943588L;
    
    @Override
    protected void processPost(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 跳转到事件消息处理接口
        final Controller controller = getController();
        controller.forward(request, response, controller.getURI(request, MsgServlet.class));
    }
    
    @Override
    protected void processGet(final HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        // 跳转到校验消息处理接口
        final Controller controller = getController();
        controller.forward(request, response, controller.getURI(request, GetMsgServlet.class));
    }
    
}
