package com.dimeng.p2p.user.servlets.financing;

import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S70.entities.T7203;
import com.dimeng.p2p.modules.bid.user.service.LctjManage;
import com.dimeng.util.parser.IntegerParser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.sql.Timestamp;

public class ExportFinancingStatisticsDetail extends AbstractFinancingServlet {

	private static final long serialVersionUID = 1L;

	@Override
	protected void processGet(final HttpServletRequest request,
							  HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		processPost(request,response,serviceSession);
	}

	@Override
	protected void processPost(final HttpServletRequest request,
			HttpServletResponse response, ServiceSession serviceSession)
			throws Throwable {
		LctjManage service=serviceSession.getService(LctjManage.class);
		response.setHeader("Content-disposition", "attachment;filename="
				+ new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
		response.setContentType("application/csv");
		Paging paging=new Paging(){
			public int getCurrentPage(){
				return IntegerParser.parse(request.getParameter(PAGING_CURRENT));
			}

			public int getSize(){
				return Integer.MAX_VALUE;
			}
		};
		String sYear = request.getParameter("sYear");
        String sMonth = request.getParameter("sMonth");
        String eYear = request.getParameter("eYear");
        String eMonth = request.getParameter("eMonth");
		
		PagingResult<T7203> reslut = service.searchDetail(sYear, sMonth, eYear, eMonth,paging);

		service.export(reslut.getItems(), response.getOutputStream(), "");
	}

}
