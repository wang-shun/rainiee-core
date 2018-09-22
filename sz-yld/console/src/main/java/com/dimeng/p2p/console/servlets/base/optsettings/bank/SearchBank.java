package com.dimeng.p2p.console.servlets.base.optsettings.bank;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S50.entities.T5020;
import com.dimeng.p2p.S50.enums.T5020_F03;
import com.dimeng.p2p.console.servlets.base.AbstractBaseServlet;
import com.dimeng.p2p.modules.base.console.service.BankManage;
import com.dimeng.p2p.modules.base.console.service.query.BankQuery;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_BASE_SEARCHBANK", isMenu = true, name = "银行设置", moduleId = "P2P_C_BASE_OPTSETTINGS_BANK",order = 0)
public class SearchBank extends AbstractBaseServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		BankManage manage = serviceSession.getService(BankManage.class);
		Paging paging = new Paging() {

			@Override
			public int getSize() {
				return 10;
			}

			@Override
			public int getCurrentPage() {
				return IntegerParser
						.parse(request.getParameter(PAGING_CURRENT));
			}
		};
		PagingResult<T5020> result = manage.search(new BankQuery() {

			@Override
			public T5020_F03 getStatus() {
				return EnumParser.parse(T5020_F03.class,
						request.getParameter("status"));
			}

			@Override
			public String getBankName() {
				return request.getParameter("name");
			}
		}, paging);
		request.setAttribute("result", result);
		forwardView(request, response, getClass());
	}

}
