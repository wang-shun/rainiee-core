/*
 * 文 件 名:  CheckNameExist.java
 * 版    权:  深圳市迪蒙网络科技有限公司
 * 描    述:  <描述>
 * 修 改 人:  xiaoqi
 * 修改时间:  2015年10月29日
 */
package com.dimeng.p2p.console.servlets.account.vipmanage.zhgl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.modules.account.console.service.ZhglManage;

/**
 * 后台添加个人用户，判断用户名是否存在
 * 
 * @author xiaoqi
 * @version [v3.1.2, 2015年10月29日]
 */
public class CheckNameExist extends AbstractAccountServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		this.processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(HttpServletRequest request, HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		response.setContentType("text/html;charset=" + getResourceProvider().getCharset());
		String loginName = request.getParameter("loginName");
		ZhglManage manage = serviceSession.getService(ZhglManage.class);
		boolean isResult = manage.isAccountExists(loginName);
		response.getWriter().println(isResult);
		response.getWriter().flush();
	}

}
