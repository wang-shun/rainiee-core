package com.dimeng.p2p.console.servlets.info.yygl.spsc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.modules.base.console.service.AdvertisementManage;
import com.dimeng.p2p.modules.base.console.service.entity.AdvertSpscRecord;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_INFO_YYGL_MENU", name = "运营管理",moduleId="P2P_C_INFO_YYGL",order=0)
public class SearchSpsc extends AbstractSpscServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request, response, serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			final HttpServletResponse response,
			final ServiceSession serviceSession) throws Throwable {
		AdvertisementManage advertisementManage = serviceSession
				.getService(AdvertisementManage.class);
		PagingResult<AdvertSpscRecord> result = advertisementManage.search(
				new Paging() {
					@Override
					public int getSize() {
						return 10;
					}

					@Override
					public int getCurrentPage() {
						return IntegerParser.parse(request
								.getParameter(PAGING_CURRENT));
					}
				});
		request.setAttribute("result", result);
		forwardView(request, response, getClass());
	}

}
