package com.dimeng.p2p.user.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.service.ServiceSession;

public class SingleLogin extends AbstractUserServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean mustAuthenticated() {
		return false;
	}

	@Override
	protected void processPost(HttpServletRequest request,HttpServletResponse response, ServiceSession serviceSession)throws Throwable {
		String result = "success";
		Session session;
		try {
			session = getResourceProvider().getResource(SessionManager.class).getSession(request, response, true);
		} catch (Exception e1) {
			//如果获取session出现异常，则进入ajax error方法
			logger.error(e1, e1);
			throw new Exception(e1);
		}
		try {
			//如果能获取到accountId说明当前会话已有帐号登录，如果登录认证也是通过的，把result标识置为failure，前台提示
			session.getAccountId();
			if (session.isAuthenticated()) {
				result = "failure";
			}
		} catch (Exception e) {
		}
		outString(response, result);
	}
	
	protected void outString(HttpServletResponse response,String obj) {
		PrintWriter out = null;
		try {
			response.setContentType("text/html;charset=UTF-8");
			out = response.getWriter();
			logger.debug(obj);
			out.write(obj);
		} catch (IOException e) {
			logger.error(e, e);
		}finally{
		    if(out != null){
		        out.flush();
	            out.close();
		    }
		}
	}

}
