package com.dimeng.p2p.app.servlets.pay.baofu.ret;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.app.servlets.AbstractAppBaseServlet;



/**
 * 回调servlet
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  tanhui
 * @version  [版本号, 2015年4月17日]
 */
public abstract class AbstractRetServlet extends AbstractAppBaseServlet
{

	private static final long serialVersionUID = 1L;
	@Override
    protected void processPost(HttpServletRequest request, HttpServletResponse response,
        final ServiceSession serviceSession)
        throws Throwable
    {
        String userAgent=request.getHeader("User-Agent").toLowerCase();
        if(userAgent.contains("micromessenger")){
            request.setAttribute("type", this.getRetName());
            request.getRequestDispatcher("/weixinPayRet.screen").forward(request, response);
        }else{
            PrintWriter print=response.getWriter();
            print.print("");
            print.close();
        }
         
    }
    
    @Override
	protected boolean mustAuthenticated() {
        return false;
	}
    abstract protected String getRetName();
}
