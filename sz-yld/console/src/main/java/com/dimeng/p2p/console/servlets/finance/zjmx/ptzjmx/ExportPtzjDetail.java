package com.dimeng.p2p.console.servlets.finance.zjmx.ptzjmx;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S61.enums.T6101_F03;
import com.dimeng.p2p.S61.enums.T6110_F06;
import com.dimeng.p2p.S61.enums.T6110_F10;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.FundsManage;
import com.dimeng.p2p.modules.account.console.service.entity.ZjDetailView;
import com.dimeng.p2p.modules.account.console.service.query.GrzjDetailQuery;
import com.dimeng.util.StringHelper;
import com.dimeng.util.parser.DateParser;
import com.dimeng.util.parser.EnumParser;
import com.dimeng.util.parser.IntegerParser;

@Right(id = "P2P_C_FINANCE_EXPORTPTZJMX", name = "导出", moduleId="P2P_C_FINANCE_PT_ZJMX", order = 1)
public class ExportPtzjDetail extends AbstractFinanceServlet {
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
		response.setHeader("Content-disposition", "attachment;filename="
				+ new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
		response.setContentType("application/csv");
		FundsManage fundsManage = serviceSession.getService(FundsManage.class);
		PagingResult<ZjDetailView> ptzjDetailViewList = fundsManage.queryGrzjDetail(
					new GrzjDetailQuery() {

						@Override
						public String getLoginName() {
							return request.getParameter("loginName");
						}

						@Override
						public int getTradingType() {
							return Integer.parseInt(StringHelper.isEmpty(request.getParameter("tradingType")) ? "-1" : request.getParameter("tradingType"));
						}

						@Override
						public Date getStartDate() {
							return DateParser.parse(request.getParameter("startTime"));
						}
						
						@Override
						public Date getEndDate() {
							return DateParser.parse(request.getParameter("endTime"));
						}

						@Override
						public T6110_F06 getUserType() {
							return T6110_F06.FZRR;
						}

						@Override
						public T6110_F10 getUserDb() {
							return T6110_F10.F;
						}

						@Override
						public boolean getPlatDetailFlg() {
							return true;
						}
						
						@Override
						public T6101_F03 getZhlx() {
							return !StringHelper.isEmpty(request.getParameter("zhlx"))?EnumParser.parse(T6101_F03.class, request.getParameter("zhlx")):null;
						}

					}, new Paging() {

					@Override
					public int getSize() {
						return Integer.MAX_VALUE;

					}

					@Override
					public int getCurrentPage() {
						return IntegerParser.parse(request
								.getParameter(PAGING_CURRENT));

					}
				});
		fundsManage.exportPtzjDetail(ptzjDetailViewList.getItems(),
				response.getOutputStream(), "");
	}
}
