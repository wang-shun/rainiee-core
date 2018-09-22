package com.dimeng.p2p.front.servlets;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.http.session.SessionManager;
import com.dimeng.framework.service.ServiceSession;

public class VerifyCommon extends AbstractFrontServlet
{
	private static final long serialVersionUID = 1L;

	@Override
	protected boolean mustAuthenticated() {
		return false;
	}

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		Session session = getResourceProvider().getResource(
				SessionManager.class).getSession(request, response, true);
		String verifyType = request.getParameter("verifyType");
		String type="";
		if("login".equals(verifyType)){
			type = "LOGIN";     //登陆
		}else if("bindEmail".equals(verifyType)){
			type = "BIND_EMAIL";                //绑定邮箱
		}else if("updateEmail".equals(verifyType)){
			type="UPDATE_EMAIL";				//修改邮箱：验证原邮箱
		}else if("newEmail".equals(verifyType)){
			type="NEW_EMAIL";					//修改邮箱：验证新邮箱
		}else if("epUpdateEmail".equals(verifyType)){
			type="EP_UPDATE_EMAIL";				//修改邮箱：验证原手机
		}else if("bindPhone".equals(verifyType)){
			type="BIND_PHONE";					//绑定手机
		}else if("updatePhone".equals(verifyType)){
            type="UPDATE_PHONE";                  //修改手机：验证原手机
        }else if("newPhone".equals(verifyType)){
            type="NEW_PHONE";                  //修改手机：验证新手机
        }else if("updatePasw".equals(verifyType)){
            type="UPDATE_PASW";                  //修改手机：验证新手机
        }
		
		session.invalidVerifyCode(type);
		final String verifyCode = session.getVerifyCode(type);
		showKaptcha(COMMON_KAPTCHA_PRODUCER, verifyCode, response);
	}
}
