package com.dimeng.p2p.front.servlets.credit.rzyx;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.resource.PromptLevel;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.p2p.S62.entities.T6282;
import com.dimeng.p2p.front.servlets.credit.AbstractCreditServlet;
import com.dimeng.p2p.modules.bid.front.service.BidWillManage;
import com.dimeng.util.parser.IntegerParser;

public class Grxy extends AbstractCreditServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, final ServiceSession serviceSession)
			throws Throwable {
		BidWillManage manage = serviceSession.getService(BidWillManage.class);
		T6282 t6282 = new T6282();
		t6282.parse(request);
		t6282.F08 = IntegerParser.parse(request.getParameter("xian"));
		manage.add(t6282);
		getController().prompt(request, response, PromptLevel.WARRING,
				"提交成功！我们将尽快和您联系");
		sendRedirect(request, response,
				getController().getViewURI(request, Grxy.class));
	}
}
