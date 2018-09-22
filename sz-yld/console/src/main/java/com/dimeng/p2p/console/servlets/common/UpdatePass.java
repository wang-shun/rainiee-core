package com.dimeng.p2p.console.servlets.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.session.Session;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.console.servlets.AbstractConsoleServlet;
import com.dimeng.p2p.console.servlets.Login;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.util.StringHelper;

public class UpdatePass extends AbstractConsoleServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		try {
			SysUserManage sysUserManage = serviceSession
					.getService(SysUserManage.class);
			String oldPassWord = StringHelper.isEmpty(request.getParameter("oldPassWord")) ? "" : request.getParameter("oldPassWord");
			String newPassWord1 = request.getParameter("newPassWord1");
			String newPassWord2 = request.getParameter("newPassWord2");
			if (StringHelper.isEmpty(newPassWord1)
					|| StringHelper.isEmpty(newPassWord2)
					|| !newPassWord1.equals(newPassWord2)) {
				throw new LogicalException("新密码不能为空,并且两次输入需要一致");
			}
            oldPassWord = RSAUtils.decryptStringByJs(oldPassWord);
            newPassWord1 = RSAUtils.decryptStringByJs(newPassWord1);
			sysUserManage.updatePassWord(oldPassWord, newPassWord1);
			Session session = serviceSession.getSession();
	        if (session != null) {
	            session.invalidateAll(request, response);
	        }
	        /*sendRedirect(request, response,
	                getController().getURI(request, Login.class));*/
			response.getWriter().print("<script>window.parent.location = '"+getController().getURI(request, Login.class)+"';</script>");
			/*SysUser sysU = sysUserManage.get(serviceSession.getSession().getAccountId());
            if(Constant.BUSINESS_ROLE_ID == sysU.roleId){
                sendRedirect(request, response,
                    getController().getURI(request, BusinessUserList.class)); 
            }else{
                sendRedirect(request, response,
                    getController().getURI(request, Index.class));
            }*/
		} catch (Exception e) {
			prompt(request, response, PromptLevel.ERROR, e.getMessage());
			forwardView(request, response, getClass());
		}
	}

}
