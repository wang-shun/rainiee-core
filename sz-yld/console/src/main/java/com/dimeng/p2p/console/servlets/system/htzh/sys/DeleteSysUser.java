package com.dimeng.p2p.console.servlets.system.htzh.sys;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.service.RoleManage;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_SYS_HTGL_GLYGL_SCGLY", name = "删除管理员",moduleId="P2P_C_SYS_HTZH_GLYGL",order=3,isDisplay=false)
public class DeleteSysUser extends AbstractSystemServlet {
	private static final long serialVersionUID = 1L;


	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		SysUserManage sysUserManage = serviceSession.getService(SysUserManage.class);
		RoleManage roleManage = serviceSession.getService(RoleManage.class);
		String name = request.getParameter("name");
		String status = request.getParameter("status");
		int id = IntegerParser.parse(request.getParameter("id"));
		int roleId = IntegerParser.parse(request.getParameter("roleId"));
		sysUserManage.update(id,name,null,null, status,roleId,null,request.getParameter("dept"));
		roleManage.setRoles(id, roleId);
		sendRedirect(request, response,getController().getURI(request, SysUserList.class));
	}

	@Override
	protected void onThrowable(HttpServletRequest request,
			HttpServletResponse response, Throwable throwable)
			throws ServletException, IOException {
		super.onThrowable(request, response, throwable);
	}
}
