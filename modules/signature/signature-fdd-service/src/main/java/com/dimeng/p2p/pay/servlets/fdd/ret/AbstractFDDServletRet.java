package com.dimeng.p2p.pay.servlets.fdd.ret;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.AbstractFddServlet;

public abstract class AbstractFDDServletRet extends AbstractFddServlet
{
    
    /**
     * 注释内容
     */
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
        throws Throwable
    {
        //处理返回结果
        handlerResult(request, response, serviceSession);
        //向第三方答复响应“success”
        doPrintWriter(response, "success", false);
    }
    
    protected abstract Object handlerResult(HttpServletRequest request, HttpServletResponse response,
        ServiceSession serviceSession)
        throws Throwable;
    
}
