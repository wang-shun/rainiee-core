package com.dimeng.p2p.console.servlets.system.htzh.sys;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.config.ConfigureProvider;
import com.dimeng.framework.http.entity.RoleBean;
import com.dimeng.framework.http.service.RoleManage;
import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.exception.LogicalException;
import com.dimeng.framework.service.exception.ParameterException;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.common.Constant;
import com.dimeng.p2p.console.servlets.system.AbstractSystemServlet;
import com.dimeng.p2p.modules.systematic.console.service.SysUserManage;
import com.dimeng.p2p.modules.systematic.console.service.entity.SysUser;
import com.dimeng.p2p.repeater.business.SysBusinessManage;
import com.dimeng.p2p.repeater.business.entity.BusinessOptLog;
import com.dimeng.p2p.variables.defines.BusinessVariavle;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_SYS_HTGL_GLYGL_XXXG", name = "信息修改",moduleId="P2P_C_SYS_HTZH_GLYGL",order=2)
public class UpdateSysUser extends AbstractSystemServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		int id = IntegerParser.parse(request.getParameter("id"));
		SysUserManage sysUserManage = serviceSession.getService(SysUserManage.class);
		SysUser sysUser = sysUserManage.get(id);
		RoleManage roleManage = serviceSession.getService(RoleManage.class);
		PagingResult<RoleBean> result = roleManage.search(null, new Paging() {

			@Override
			public int getSize() {
				return Integer.MAX_VALUE;
			}

			@Override
			public int getCurrentPage() {
				return 1;
			}
		});
		final ConfigureProvider configureProvider = getResourceProvider().getResource(ConfigureProvider.class);
        boolean is_business = Boolean.parseBoolean(configureProvider.getProperty(BusinessVariavle.IS_BUSINESS));
		if(Constant.BUSINESS_ROLE_ID == sysUser.roleId && is_business){
		    SysBusinessManage sysBusinessManage = serviceSession.getService(SysBusinessManage.class);
    		BusinessOptLog logs[] = sysBusinessManage.searchLogs(sysUser.employNum);
    		request.setAttribute("logs", logs);
		}
		request.setAttribute("roles", result.getItems());
		request.setAttribute("sysUser", sysUser);
		
		forwardView(request, response, getClass());
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		SysUserManage sysUserManage = serviceSession.getService(SysUserManage.class);
		RoleManage roleManage = serviceSession.getService(RoleManage.class);
		String name = request.getParameter("name");
		String status = request.getParameter("status");
		String phone = request.getParameter("phone");
        String pos = request.getParameter("pos");
        String empployNum = request.getParameter("employNum");
		int id = IntegerParser.parse(request.getParameter("id"));
		int roleId = IntegerParser.parse(request.getParameter("roleId"));
		try{
	        SysUser sysUser = sysUserManage.get(id);
	        sysUserManage.update(id,name, status,phone,pos,sysUser.roleId,empployNum,request.getParameter("dept"));
			if(roleId>0){
			    roleManage.setRoles(id, roleId);
			}
			sendRedirect(request, response,getController().getURI(request, SysUserList.class));
		} catch (Throwable e) {
			if (e instanceof ParameterException || e instanceof LogicalException) {
				getController().prompt(request, response, PromptLevel.WARRING,e.getMessage());
				processGet(request, response, serviceSession);
			} else {
				super.onThrowable(request, response, e);
			}
		}
	}
}
