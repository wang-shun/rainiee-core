package com.dimeng.p2p.console.servlets.finance.czgl.xxczgl;

import java.sql.Date;
import java.sql.Timestamp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dimeng.framework.http.servlet.annotation.Right;
import com.dimeng.framework.service.ServiceSession;
import com.dimeng.framework.service.query.Paging;
import com.dimeng.framework.service.query.PagingResult;
import com.dimeng.p2p.S71.enums.T7150_F05;
import com.dimeng.p2p.console.servlets.finance.AbstractFinanceServlet;
import com.dimeng.p2p.modules.account.console.service.OfflineChargeManage;
import com.dimeng.p2p.modules.account.console.service.entity.OfflineChargeRecord;
import com.dimeng.p2p.modules.account.console.service.query.OfflineChargeExtendsQuery;
import com.dimeng.util.parser.IntegerParser;
import com.dimeng.util.parser.SQLDateParser;

@Right(id = "P2P_C_FINANCE_EXPORTXXCZGL", name = "导出", moduleId="P2P_C_FINANCE_CZGL_XXCZGL",order = 3)
public class ExportXxczgl extends AbstractFinanceServlet {
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
		OfflineChargeManage chargeManage = serviceSession
				.getService(OfflineChargeManage.class);
		response.setHeader("Content-disposition", "attachment;filename="
				+ new Timestamp(System.currentTimeMillis()).getTime() + ".csv");
		response.setContentType("application/csv");
		PagingResult<OfflineChargeRecord> result = chargeManage.search(
				new OfflineChargeExtendsQuery() {

					@Override
					public String getAccount() {
						return request.getParameter("account");
					}
					
					@Override
					public String getCreateAccount() {
						return request.getParameter("createAccount");
					}
					
					@Override
					public String getAuditor() {
						return request.getParameter("auditor");
					}
					
					@Override
					public T7150_F05 getStatus() {
						return T7150_F05.parse(request.getParameter("status"));
					}

					@Override
					public Date getCreateStartDate() {
						return SQLDateParser.parse(request
								.getParameter("startTime"));
					}

					@Override
					public Date getCreateEndDate() {
						return SQLDateParser.parse(request
								.getParameter("endTime"));
					}
					
					@Override
					public Date getAuditorStartDate() {
						return SQLDateParser.parse(request
								.getParameter("auditorStartTime"));
					}

					@Override
					public Date getAuditorEndDate() {
						return SQLDateParser.parse(request
								.getParameter("auditorEndTime"));
					}

					@Override
					public String getTelPhone() {
						return request.getParameter("telPhone");
					}

					@Override
					public String getName() {
						return request.getParameter("name");
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
		chargeManage.export(result.getItems(), response.getOutputStream(), "");
	}
}