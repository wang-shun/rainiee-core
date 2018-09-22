package com.dimeng.p2p.console.servlets.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.common.RSAUtils;
import com.dimeng.p2p.console.servlets.AbstractConsoleServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.SysUser;
import com.dimeng.util.StringHelper;

public class Update extends AbstractConsoleServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		SysUserManage sysUserManage = serviceSession
				.getService(SysUserManage.class);
		SysUser sysU = sysUserManage.get(serviceSession.getSession().getAccountId());
		try {

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

			if(Constant.BUSINESS_ROLE_ID == sysU.roleId) {
				sendRedirect(request, response,"/console/system/htzh/business/businessUserList.htm?isLoad=true");
			}else{
				sendRedirect(request, response,
						getController().getURI(request, Index.class) + "?isLoad=true");
			}
		} catch (Exception e) {
			prompt(request, response, PromptLevel.WARRING, e.getMessage());
			if(Constant.BUSINESS_ROLE_ID == sysU.roleId) {
				sendRedirect(request, response,"/console/system/htzh/business/businessUserList.htm");
			}else{
				sendRedirect(request, response,
						getController().getURI(request, Index.class));
			}
		}
	}

}
