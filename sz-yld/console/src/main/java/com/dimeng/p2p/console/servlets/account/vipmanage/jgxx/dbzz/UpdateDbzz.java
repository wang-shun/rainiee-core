package com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.dbzz;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.console.servlets.account.AbstractAccountServlet;
import com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.JgList;
import com.dimeng.p2p.console.servlets.account.vipmanage.jgxx.jscl.UpdateJscl;
import com.dimeng.p2p.modules.account.console.service.QyManage;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_ACCOUNT_UPDATEJGXX", name = "修改",moduleId="P2P_C_ACCOUNT_JGXX",order=2)
public class UpdateDbzz extends AbstractAccountServlet {

	private static final long serialVersionUID = 1L;
	
	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		QyManage manage = serviceSession.getService(QyManage.class);
		int id = IntegerParser.parse(request.getParameter("id"));
		String content = manage.getDbzz(id);
		request.setAttribute("info", content);
		forwardView(request, response, getClass());
	}
	
	@Override
	protected void processPost(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		try
        {
            QyManage manage = serviceSession.getService(QyManage.class);
            int id = IntegerParser.parse(request.getParameter("id"));
            String content = request.getParameter("dbzz");
            manage.updateDbzz(content, id);

            if("submit".equals(request.getParameter("entryType"))){
                sendRedirect(request, response, getController().getURI(request, JgList.class));
                return;
            }
            sendRedirect(request, response, getController().getURI(request, UpdateJscl.class) + "?id=" + id);
        }
        catch (Exception e)
        {
            prompt(request, response, PromptLevel.ERROR, e.getMessage());
            processGet(request, response, serviceSession);
        }
	}
}
